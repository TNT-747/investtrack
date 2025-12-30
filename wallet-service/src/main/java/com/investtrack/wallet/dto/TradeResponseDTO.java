package com.investtrack.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Trade Response DTO - Result of a trade operation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponseDTO {
    private boolean success;
    private String message;
    private WalletDTO wallet;
    private TransactionDTO transaction;
}
