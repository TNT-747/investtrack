package com.investtrack.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Wallet Entity - Represents a user's holding of a specific asset
 */
@Entity
@Table(name = "wallets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"userId", "assetSymbol"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User ID is required")
    @Column(nullable = false, length = 50)
    private String userId;

    @NotBlank(message = "Asset symbol is required")
    @Column(nullable = false, length = 10)
    private String assetSymbol;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal quantity;

    @NotNull(message = "Average buy price is required")
    @PositiveOrZero(message = "Average buy price must be zero or positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal averageBuyPrice;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
