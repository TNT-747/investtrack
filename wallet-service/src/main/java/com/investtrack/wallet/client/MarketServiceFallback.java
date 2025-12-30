package com.investtrack.wallet.client;

import com.investtrack.wallet.dto.AssetDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for Market Service Client
 * Used when circuit breaker is open
 */
@Component
@Slf4j
public class MarketServiceFallback implements MarketServiceClient {

    @Override
    public AssetDTO getAssetBySymbol(String symbol) {
        log.warn("Market Service is unavailable. Returning fallback for symbol: {}", symbol);
        // Return null to indicate service unavailability
        // The service layer will handle this appropriately
        return null;
    }
}
