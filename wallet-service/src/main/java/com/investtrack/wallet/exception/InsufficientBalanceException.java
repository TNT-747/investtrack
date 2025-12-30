package com.investtrack.wallet.exception;

/**
 * Exception thrown when user has insufficient balance for a sell operation
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
