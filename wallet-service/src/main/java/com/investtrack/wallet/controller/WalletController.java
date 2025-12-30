package com.investtrack.wallet.controller;

import com.investtrack.wallet.dto.*;
import com.investtrack.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Wallet REST Controller - API endpoints for wallet and transaction management
 */
@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    /**
     * Get user's portfolio
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WalletDTO>> getUserPortfolio(@PathVariable String userId) {
        log.info("GET /api/wallets/user/{} - Fetching user portfolio", userId);
        List<WalletDTO> portfolio = walletService.getUserPortfolio(userId);
        return ResponseEntity.ok(portfolio);
    }

    /**
     * Execute a trade (Buy or Sell)
     */
    @PostMapping("/trade")
    public ResponseEntity<TradeResponseDTO> executeTrade(@Valid @RequestBody TradeRequestDTO tradeRequest) {
        log.info("POST /api/wallets/trade - Executing {} trade for user {}", 
                tradeRequest.getType(), tradeRequest.getUserId());
        
        TradeResponseDTO response = walletService.executeTrade(tradeRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }

    /**
     * Get user's transaction history (filtered by config history-days)
     */
    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getUserTransactionHistory(@PathVariable String userId) {
        log.info("GET /api/wallets/user/{}/transactions - Fetching transaction history", userId);
        List<TransactionDTO> transactions = walletService.getUserTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }
}
