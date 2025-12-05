package com.groupa.chickendirectfarm.exception;

public class DuplicateProductInPurchaseException extends RuntimeException {
    public DuplicateProductInPurchaseException(String message) {
        super(message);
    }
}
