package ma.portfolio_service.repository;

import ma.portfolio_service.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByPortfolioId(Long portfolioId);

    List<Position> findBySymbol(String symbol);

    List<Position> findByAssetType(String assetType);
}
