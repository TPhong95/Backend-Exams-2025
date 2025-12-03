package com.groupa.chickendirectfarm.exception;

public class OutOfStockExpection extends IllegalArgumentException{
    public OutOfStockExpection(String message) {
        super(message);
    }
}
