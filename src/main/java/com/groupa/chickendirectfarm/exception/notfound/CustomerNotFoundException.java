package com.groupa.chickendirectfarm.exception.notfound;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
