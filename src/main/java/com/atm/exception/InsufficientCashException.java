package com.atm.exception;

public class InsufficientCashException extends Exception {
    public InsufficientCashException(String message) {
        super(message);
    }
    
    public InsufficientCashException(String message, Throwable cause) {
        super(message, cause);
    }
}