package com.investtrack.market.exception;

/**
 * Exception thrown when trying to create an asset that already exists
 */
public class AssetAlreadyExistsException extends RuntimeException {
    public AssetAlreadyExistsException(String message) {
        super(message);
    }
}
