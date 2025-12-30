package com.investtrack

.wallet.service;

import com.investtrack.wallet.client.MarketServiceClient;
import com.investtrack.wallet.config.WalletConfig;
import com.investtrack.wallet.dto.*;
import com.investtrack.wallet.entity.Transaction;
import com.investtrack.wallet.entity.Wallet;
import com.investtrack.wallet.exception.InsufficientBalanceException;
import com.investtrack.wallet.exception.MarketServiceUnavailableException;
import com.investtrack.wallet.repository.TransactionRepository;
import com.investtrack.wallet.repository.WalletRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wallet Service - Business logic for portfolio and transaction management
 * Uses Circuit Breaker pattern for Market Service calls
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final MarketServiceClient marketServiceClient;
    private final WalletConfig walletConfig;

    /**
     * Get user's complete portfolio
     */
    @Transactional(readOnly = true)
    public List<WalletDTO> getUserPortfolio(String userId) {
        log.info("Fetching portfolio for user: {}", userId);
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        
        return wallets.stream()
                .map(this::convertToWalletDTO)
                .collect(Collectors.toList());
    }

    /**
     * Execute a trade (BUY or SELL)
     * Uses Circuit Breaker to protect against Market Service failures
     */
    @CircuitBreaker(name = "marketService", fallbackMethod = "tradeFallback")
    public TradeResponseDTO executeTrade(TradeRequestDTO tradeRequest) {
        log.info("Executing {} trade for user {} - Asset: {}, Quantity: {}", 
                tradeRequest.getType(), tradeRequest.getUserId(), 
                tradeRequest.getAssetSymbol(), tradeRequest.getQuantity());

        // Validate asset exists and get current price from Market Service
        AssetDTO asset = marketServiceClient.getAssetBySymbol(tradeRequest.getAssetSymbol().toUpperCase());
        
        if (asset == null) {
            throw new MarketServiceUnavailableException("Market Service is currently unavailable. Please try again later.");
        }

        log.info("Asset {} validated. Current price: {}", asset.getSymbol(), asset.getCurrentPrice());

        // Execute trade based on type
        if (tradeRequest.getType() == Transaction.TransactionType.BUY) {
            return executeBuy(tradeRequest, asset);
        } else {
            return executeSell(tradeRequest, asset);
        }
    }

    /**
     * Execute BUY transaction
     */
    private TradeResponseDTO executeBuy(TradeRequestDTO tradeRequest, AssetDTO asset) {
        String userId = tradeRequest.getUserId();
        String symbol = asset.getSymbol();
        BigDecimal quantity = tradeRequest.getQuantity();
        BigDecimal price = asset.getCurrentPrice();

        // Find or create wallet
        Wallet wallet = walletRepository.findByUserIdAndAssetSymbol(userId, symbol)
                .orElse(new Wallet(null, userId, symbol, BigDecimal.ZERO, BigDecimal.ZERO, List.of()));

        // Calculate new average buy price
        BigDecimal totalValue = wallet.getQuantity().multiply(wallet.getAverageBuyPrice());
        BigDecimal newValue = quantity.multiply(price);
        BigDecimal newQuantity = wallet.getQuantity().add(quantity);
        BigDecimal newAveragePrice = totalValue.add(newValue).divide(newQuantity, 2, RoundingMode.HALF_UP);

        wallet.setQuantity(newQuantity);
        wallet.setAverageBuyPrice(newAveragePrice);

        Wallet savedWallet = walletRepository.save(wallet);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setWallet(savedWallet);
        transaction.setType(Transaction.TransactionType.BUY);
        transaction.setAssetSymbol(symbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("BUY transaction completed successfully. New quantity: {}, New avg price: {}", 
                newQuantity, newAveragePrice);

        return new TradeResponseDTO(
                true,
                "Buy order executed successfully",
                convertToWalletDTO(savedWallet),
                convertToTransactionDTO(savedTransaction)
        );
    }

    /**
     * Execute SELL transaction
     */
    private TradeResponseDTO executeSell(TradeRequestDTO tradeRequest, AssetDTO asset) {
        String userId = tradeRequest.getUserId();
        String symbol = asset.getSymbol();
        BigDecimal quantity = tradeRequest.getQuantity();
        BigDecimal price = asset.getCurrentPrice();

        // Find wallet
        Wallet wallet = walletRepository.findByUserIdAndAssetSymbol(userId, symbol)
                .orElseThrow(() -> new InsufficientBalanceException("You don't own any " + symbol));

        // Check sufficient balance
        if (wallet.getQuantity().compareTo(quantity) < 0) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. You have %s but trying to sell %s", 
                            wallet.getQuantity(), quantity));
        }

        // Update wallet quantity
        BigDecimal newQuantity = wallet.getQuantity().subtract(quantity);
        wallet.setQuantity(newQuantity);

        // If quantity becomes zero, we can delete the wallet or keep it
        Wallet savedWallet = walletRepository.save(wallet);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setWallet(savedWallet);
        transaction.setType(Transaction.TransactionType.SELL);
        transaction.setAssetSymbol(symbol);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("SELL transaction completed successfully. Remaining quantity: {}", newQuantity);

        return new TradeResponseDTO(
                true,
                "Sell order executed successfully",
                convertToWalletDTO(savedWallet),
                convertToTransactionDTO(savedTransaction)
        );
    }

    /**
     * Fallback method for circuit breaker
     * Called when Market Service is unavailable
     */
    public TradeResponseDTO tradeFallback(TradeRequestDTO tradeRequest, Exception ex) {
        log.error("Circuit breaker activated. Market Service is unavailable: {}", ex.getMessage());
        
        return new TradeResponseDTO(
                false,
                "Market Service is currently unavailable. Please try again later.",
                null,
                null
        );
    }

    /**
     * Get user's transaction history
     * Filtered by the configurable history-days parameter
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> getUserTransactionHistory(String userId) {
        log.info("Fetching transaction history for user: {} (last {} days)", 
                userId, walletConfig.getHistoryDays());

        // Calculate cutoff date based on config
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(walletConfig.getHistoryDays());

        // Fetch filtered transactions
        List<Transaction> transactions = transactionRepository.findByUserIdAndTimestampAfter(userId, cutoffDate);

        log.info("Found {} transactions for user {} in the last {} days", 
                transactions.size(), userId, walletConfig.getHistoryDays());

        return transactions.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Wallet entity to DTO
     */
    private WalletDTO convertToWalletDTO(Wallet wallet) {
        return new WalletDTO(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getAssetSymbol(),
                wallet.getQuantity(),
                wallet.getAverageBuyPrice()
        );
    }

    /**
     * Convert Transaction entity to DTO
     */
    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getWallet().getId(),
                transaction.getType(),
                transaction.getAssetSymbol(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getTimestamp()
        );
    }
}
