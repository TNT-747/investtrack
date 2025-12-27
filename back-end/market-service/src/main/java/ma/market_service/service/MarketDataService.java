package ma.market_service.service;

import lombok.RequiredArgsConstructor;
import ma.market_service.entity.MarketData;
import ma.market_service.repository.MarketDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketDataService {

    private final MarketDataRepository marketDataRepository;

    public List<MarketData> getAllMarketData() {
        return marketDataRepository.findAll();
    }

    public Optional<MarketData> getMarketDataById(Long id) {
        return marketDataRepository.findById(id);
    }

    public Optional<MarketData> getMarketDataBySymbol(String symbol) {
        return marketDataRepository.findBySymbol(symbol.toUpperCase());
    }

    public List<MarketData> getMarketDataByAssetType(String assetType) {
        return marketDataRepository.findByAssetType(assetType);
    }

    public List<MarketData> searchByName(String name) {
        return marketDataRepository.findByNameContainingIgnoreCase(name);
    }

    public MarketData createMarketData(MarketData marketData) {
        marketData.setSymbol(marketData.getSymbol().toUpperCase());
        return marketDataRepository.save(marketData);
    }

    public MarketData updatePrice(String symbol, BigDecimal newPrice) {
        return marketDataRepository.findBySymbol(symbol.toUpperCase())
                .map(data -> {
                    data.setPreviousClose(data.getCurrentPrice());
                    data.setCurrentPrice(newPrice);
                    if (data.getPreviousClose() != null && data.getPreviousClose().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal change = newPrice.subtract(data.getPreviousClose())
                                .divide(data.getPreviousClose(), 4, java.math.RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));
                        data.setChangePercent(change);
                    }
                    return marketDataRepository.save(data);
                })
                .orElseThrow(() -> new RuntimeException("Market data not found for symbol: " + symbol));
    }

    public MarketData updateMarketData(Long id, MarketData marketDataDetails) {
        return marketDataRepository.findById(id)
                .map(data -> {
                    data.setName(marketDataDetails.getName());
                    data.setCurrentPrice(marketDataDetails.getCurrentPrice());
                    data.setOpenPrice(marketDataDetails.getOpenPrice());
                    data.setHighPrice(marketDataDetails.getHighPrice());
                    data.setLowPrice(marketDataDetails.getLowPrice());
                    data.setVolume(marketDataDetails.getVolume());
                    data.setExchange(marketDataDetails.getExchange());
                    data.setCurrency(marketDataDetails.getCurrency());
                    return marketDataRepository.save(data);
                })
                .orElseThrow(() -> new RuntimeException("Market data not found with id: " + id));
    }

    public void deleteMarketData(Long id) {
        marketDataRepository.deleteById(id);
    }
}
