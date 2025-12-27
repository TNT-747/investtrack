package ma.market_service.controller;

import lombok.RequiredArgsConstructor;
import ma.market_service.entity.MarketData;
import ma.market_service.service.MarketDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataService marketDataService;

    @GetMapping
    public ResponseEntity<List<MarketData>> getAllMarketData() {
        return ResponseEntity.ok(marketDataService.getAllMarketData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketData> getMarketDataById(@PathVariable Long id) {
        return marketDataService.getMarketDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<MarketData> getMarketDataBySymbol(@PathVariable String symbol) {
        return marketDataService.getMarketDataBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{assetType}")
    public ResponseEntity<List<MarketData>> getMarketDataByAssetType(@PathVariable String assetType) {
        return ResponseEntity.ok(marketDataService.getMarketDataByAssetType(assetType));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MarketData>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(marketDataService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<MarketData> createMarketData(@RequestBody MarketData marketData) {
        MarketData created = marketDataService.createMarketData(marketData);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketData> updateMarketData(@PathVariable Long id, @RequestBody MarketData marketData) {
        try {
            MarketData updated = marketDataService.updateMarketData(id, marketData);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/symbol/{symbol}/price")
    public ResponseEntity<MarketData> updatePrice(@PathVariable String symbol, @RequestParam BigDecimal price) {
        try {
            MarketData updated = marketDataService.updatePrice(symbol, price);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketData(@PathVariable Long id) {
        marketDataService.deleteMarketData(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "market-service");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
