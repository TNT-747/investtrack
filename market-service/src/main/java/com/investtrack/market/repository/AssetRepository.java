package com.investtrack.market.repository;

import com.investtrack.market.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Asset Repository - Data access layer for Asset entity
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Find asset by symbol
     */
    Optional<Asset> findBySymbol(String symbol);

    /**
     * Find all assets by type
     */
    List<Asset> findByType(Asset.AssetType type);

    /**
     * Check if asset exists by symbol
     */
    boolean existsBySymbol(String symbol);
}
