package com.investtrack.wallet.repository;

import com.investtrack.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Wallet Repository - Data access layer for Wallet entity
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Find all wallets for a user
     */
    List<Wallet> findByUserId(String userId);

    /**
     * Find specific wallet for user and asset
     */
    Optional<Wallet> findByUserIdAndAssetSymbol(String userId, String assetSymbol);

    /**
     * Check if wallet exists
     */
    boolean existsByUserIdAndAssetSymbol(String userId, String assetSymbol);
}
