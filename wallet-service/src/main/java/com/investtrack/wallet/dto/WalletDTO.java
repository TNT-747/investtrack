package com.investtrack.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Wallet Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    private Long id;
    private String userId;
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal averageBuyPrice;
}
