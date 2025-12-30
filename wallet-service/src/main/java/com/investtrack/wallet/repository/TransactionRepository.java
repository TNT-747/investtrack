package com.investtrack.wallet.repository;

import com.investtrack.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Transaction Repository - Data access layer for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transactions by wallet ID ordered by timestamp descending
     */
    List<Transaction> findByWalletIdOrderByTimestampDesc(Long walletId);

    /**
     * Find transactions for a wallet after a specific date
     * This is used for the configurable history-days filtering
     */
    List<Transaction> findByWalletIdAndTimestampAfterOrderByTimestampDesc(
        Long walletId, 
        LocalDateTime after
    );

    /**
     * Find all transactions for a user (across all wallets) after a date
     */
    @Query("SELECT t FROM Transaction t WHERE t.wallet.userId = :userId AND t.timestamp > :after ORDER BY t.timestamp DESC")
    List<Transaction> findByUserIdAndTimestampAfter(
        @Param("userId") String userId, 
        @Param("after") LocalDateTime after
    );
}
