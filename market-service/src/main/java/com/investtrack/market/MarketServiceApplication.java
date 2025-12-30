package com.investtrack.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Market Service - Asset Catalog Management
 * 
 * Manages the catalog of available financial assets (stocks, crypto, commodities)
 * with CRUD operations and price updates.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MarketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketServiceApplication.class, args);
    }
}
