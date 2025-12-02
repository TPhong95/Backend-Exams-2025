package com.groupa.chickendirectfarm.exception;

public abstract class BasicNotFoundException extends RuntimeException {
    public BasicNotFoundException(String message) {
        super(message);
    }
}
