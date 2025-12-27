package ma.market_service.repository;

import ma.market_service.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findBySymbol(String symbol);

    List<PriceHistory> findBySymbolAndDateBetween(String symbol, LocalDate startDate, LocalDate endDate);

    List<PriceHistory> findBySymbolOrderByDateDesc(String symbol);
}
