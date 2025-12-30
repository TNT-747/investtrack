package com.investtrack.market.exception;

/**
 * Exception thrown when an asset is not found
 */
public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String message) {
        super(message);
    }
}
