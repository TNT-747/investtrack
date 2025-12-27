package ma.market_service.repository;

import ma.market_service.entity.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {

    Optional<MarketData> findBySymbol(String symbol);

    List<MarketData> findByAssetType(String assetType);

    List<MarketData> findByExchange(String exchange);

    List<MarketData> findByNameContainingIgnoreCase(String name);
}
