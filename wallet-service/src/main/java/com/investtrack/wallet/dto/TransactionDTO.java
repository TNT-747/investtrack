package com.investtrack.wallet.dto;

import com.investtrack.wallet.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private Long walletId;
    private Transaction.TransactionType type;
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
