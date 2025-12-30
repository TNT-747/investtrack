package com.investtrack.wallet.dto;

import com.investtrack.wallet.entity.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Trade Request DTO - For buy/sell transactions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDTO {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Asset symbol is required")
    private String assetSymbol;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @NotNull(message = "Transaction type is required")
    private Transaction.TransactionType type;
}
