package com.groupa.chickendirectfarm.exception.notfound;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
