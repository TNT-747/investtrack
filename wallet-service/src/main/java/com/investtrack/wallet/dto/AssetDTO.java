package com.investtrack.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Asset DTO - Mirror of Market Service AssetDTO for Feign communication
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDTO {
    private Long id;
    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    private String type;
}
