package com.groupa.chickendirectfarm.exception.conflict;

public class PurchaseAlreadyHandledException extends RuntimeException {
    public PurchaseAlreadyHandledException(String message) { super(message); }
}
