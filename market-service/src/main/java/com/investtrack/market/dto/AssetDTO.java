package com.investtrack.market.dto;

import com.investtrack.market.entity.Asset;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Asset Data Transfer Object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDTO {

    private Long id;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Current price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal currentPrice;

    @NotNull(message = "Asset type is required")
    private Asset.AssetType type;

    /**
     * Convert Entity to DTO
     */
    public static AssetDTO fromEntity(Asset asset) {
        return new AssetDTO(
            asset.getId(),
            asset.getSymbol(),
            asset.getName(),
            asset.getCurrentPrice(),
            asset.getType()
        );
    }

    /**
     * Convert DTO to Entity
     */
    public Asset toEntity() {
        return new Asset(
            this.id,
            this.symbol,
            this.name,
            this.currentPrice,
            this.type
        );
    }
}
