package com.investtrack.wallet.client;

import com.investtrack.wallet.dto.AssetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Market Service Feign Client - Communicates with Market Service via Eureka
 */
@FeignClient(name = "market-service", fallback = MarketServiceFallback.class)
public interface MarketServiceClient {

    /**
     * Get asset by symbol from Market Service
     */
    @GetMapping("/api/assets/symbol/{symbol}")
    AssetDTO getAssetBySymbol(@PathVariable("symbol") String symbol);
}
