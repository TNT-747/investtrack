package com.investtrack.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Wallet Service - Portfolio and Transaction Management
 * 
 * Manages user portfolios, buy/sell transactions, and transaction history
 * with OpenFeign integration to Market Service.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WalletServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletServiceApplication.class, args);
    }
}
