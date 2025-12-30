package com.investtrack.market.service;

import com.investtrack.market.dto.AssetDTO;
import com.investtrack.market.entity.Asset;
import com.investtrack.market.exception.AssetAlreadyExistsException;
import com.investtrack.market.exception.AssetNotFoundException;
import com.investtrack.market.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Asset Service - Business logic for asset management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AssetService {

    private final AssetRepository assetRepository;

    /**
     * Get all assets
     */
    @Transactional(readOnly = true)
    public List<AssetDTO> getAllAssets() {
        log.info("Fetching all assets");
        return assetRepository.findAll()
                .stream()
                .map(AssetDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get asset by ID
     */
    @Transactional(readOnly = true)
    public AssetDTO getAssetById(Long id) {
        log.info("Fetching asset by ID: {}", id);
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + id));
        return AssetDTO.fromEntity(asset);
    }

    /**
     * Get asset by symbol
     */
    @Transactional(readOnly = true)
    public AssetDTO getAssetBySymbol(String symbol) {
        log.info("Fetching asset by symbol: {}", symbol);
        Asset asset = assetRepository.findBySymbol(symbol.toUpperCase())
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with symbol: " + symbol));
        return AssetDTO.fromEntity(asset);
    }

    /**
     * Get assets by type
     */
    @Transactional(readOnly = true)
    public List<AssetDTO> getAssetsByType(Asset.AssetType type) {
        log.info("Fetching assets by type: {}", type);
        return assetRepository.findByType(type)
                .stream()
                .map(AssetDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Create new asset
     */
    public AssetDTO createAsset(AssetDTO assetDTO) {
        log.info("Creating new asset: {}", assetDTO.getSymbol());
        
        // Check if symbol already exists
        if (assetRepository.existsBySymbol(assetDTO.getSymbol().toUpperCase())) {
            throw new AssetAlreadyExistsException("Asset already exists with symbol: " + assetDTO.getSymbol());
        }

        Asset asset = assetDTO.toEntity();
        asset.setSymbol(asset.getSymbol().toUpperCase());
        Asset savedAsset = assetRepository.save(asset);
        
        log.info("Asset created successfully: {}", savedAsset.getSymbol());
        return AssetDTO.fromEntity(savedAsset);
    }

    /**
     * Update asset
     */
    public AssetDTO updateAsset(Long id, AssetDTO assetDTO) {
        log.info("Updating asset ID: {}", id);
        
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + id));

        // Update fields
        existingAsset.setName(assetDTO.getName());
        existingAsset.setCurrentPrice(assetDTO.getCurrentPrice());
        existingAsset.setType(assetDTO.getType());

        Asset updatedAsset = assetRepository.save(existingAsset);
        log.info("Asset updated successfully: {}", updatedAsset.getSymbol());
        
        return AssetDTO.fromEntity(updatedAsset);
    }

    /**
     * Update asset price only
     */
    public AssetDTO updateAssetPrice(Long id, BigDecimal newPrice) {
        log.info("Updating price for asset ID: {} to {}", id, newPrice);
        
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found with ID: " + id));

        asset.setCurrentPrice(newPrice);
        Asset updatedAsset = assetRepository.save(asset);
        
        log.info("Price updated successfully for {}", updatedAsset.getSymbol());
        return AssetDTO.fromEntity(updatedAsset);
    }

    /**
     * Delete asset
     */
    public void deleteAsset(Long id) {
        log.info("Deleting asset ID: {}", id);
        
        if (!assetRepository.existsById(id)) {
            throw new AssetNotFoundException("Asset not found with ID: " + id);
        }

        assetRepository.deleteById(id);
        log.info("Asset deleted successfully");
    }
}
