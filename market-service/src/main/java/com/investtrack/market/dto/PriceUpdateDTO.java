package com.investtrack.market.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Price Update Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdateDTO {

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal currentPrice;
}
