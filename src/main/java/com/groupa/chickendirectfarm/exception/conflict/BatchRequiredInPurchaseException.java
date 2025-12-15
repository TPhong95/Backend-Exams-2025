package com.groupa.chickendirectfarm.exception.conflict;

public class BatchRequiredInPurchaseException extends RuntimeException {
    public BatchRequiredInPurchaseException(String message) {
        super(message);
    }
}
