package com.investtrack.wallet.exception;

/**
 * Exception thrown when Market Service is unavailable
 */
public class MarketServiceUnavailableException extends RuntimeException {
    public MarketServiceUnavailableException(String message) {
        super(message);
    }
}
