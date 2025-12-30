package com.investtrack.wallet.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Wallet Configuration - Custom configuration properties
 */
@Configuration
@Data
public class WalletConfig {

    /**
     * Number of days for transaction history filtering
     * This is the MANDATORY configuration variable from Config Server
     */
    @Value("${invest-config.wallet.history-days:30}")
    private int historyDays;
}
