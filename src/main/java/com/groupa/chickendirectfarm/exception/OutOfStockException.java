package com.groupa.chickendirectfarm.exception;

public class OutOfStockException extends IllegalArgumentException{
    public OutOfStockException(String message) {
        super(message);
    }
}
