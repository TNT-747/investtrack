package ma.portfolio_service.service;

import lombok.RequiredArgsConstructor;
import ma.portfolio_service.entity.Portfolio;
import ma.portfolio_service.repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    public List<Portfolio> getPortfoliosByUserId(String userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public Portfolio createPortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public Portfolio updatePortfolio(Long id, Portfolio portfolioDetails) {
        return portfolioRepository.findById(id)
                .map(portfolio -> {
                    portfolio.setName(portfolioDetails.getName());
                    portfolio.setDescription(portfolioDetails.getDescription());
                    portfolio.setTotalValue(portfolioDetails.getTotalValue());
                    portfolio.setTotalCost(portfolioDetails.getTotalCost());
                    return portfolioRepository.save(portfolio);
                })
                .orElseThrow(() -> new RuntimeException("Portfolio not found with id: " + id));
    }

    public void deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
    }
}
