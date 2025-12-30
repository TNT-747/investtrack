package com.investtrack.market.controller;

import com.investtrack.market.dto.AssetDTO;
import com.investtrack.market.dto.PriceUpdateDTO;
import com.investtrack.market.entity.Asset;
import com.investtrack.market.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Asset REST Controller - API endpoints for asset management
 */
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {

    private final AssetService assetService;

    /**
     * Get all assets
     */
    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAllAssets() {
        log.info("GET /api/assets - Fetching all assets");
        List<AssetDTO> assets = assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    /**
     * Get asset by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetById(@PathVariable Long id) {
        log.info("GET /api/assets/{} - Fetching asset by ID", id);
        AssetDTO asset = assetService.getAssetById(id);
        return ResponseEntity.ok(asset);
    }

    /**
     * Get asset by symbol
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<AssetDTO> getAssetBySymbol(@PathVariable String symbol) {
        log.info("GET /api/assets/symbol/{} - Fetching asset by symbol", symbol);
        AssetDTO asset = assetService.getAssetBySymbol(symbol);
        return ResponseEntity.ok(asset);
    }

    /**
     * Get assets by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AssetDTO>> getAssetsByType(@PathVariable Asset.AssetType type) {
        log.info("GET /api/assets/type/{} - Fetching assets by type", type);
        List<AssetDTO> assets = assetService.getAssetsByType(type);
        return ResponseEntity.ok(assets);
    }

    /**
     * Create new asset
     */
    @PostMapping
    public ResponseEntity<AssetDTO> createAsset(@Valid @RequestBody AssetDTO assetDTO) {
        log.info("POST /api/assets - Creating asset: {}", assetDTO.getSymbol());
        AssetDTO createdAsset = assetService.createAsset(assetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    /**
     * Update asset
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetDTO assetDTO) {
        log.info("PUT /api/assets/{} - Updating asset", id);
        AssetDTO updatedAsset = assetService.updateAsset(id, assetDTO);
        return ResponseEntity.ok(updatedAsset);
    }

    /**
     * Update asset price only
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<AssetDTO> updateAssetPrice(
            @PathVariable Long id,
            @Valid @RequestBody PriceUpdateDTO priceUpdate) {
        log.info("PATCH /api/assets/{}/price - Updating price to {}", id, priceUpdate.getCurrentPrice());
        AssetDTO updatedAsset = assetService.updateAssetPrice(id, priceUpdate.getCurrentPrice());
        return ResponseEntity.ok(updatedAsset);
    }

    /**
     * Delete asset
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        log.info("DELETE /api/assets/{} - Deleting asset", id);
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
