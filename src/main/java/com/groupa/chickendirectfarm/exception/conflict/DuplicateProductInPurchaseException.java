package com.groupa.chickendirectfarm.exception.conflict;

public class DuplicateProductInPurchaseException extends RuntimeException {
    public DuplicateProductInPurchaseException(String message) {
        super(message);
    }
}
