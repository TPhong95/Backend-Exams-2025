package com.groupa.chickendirectfarm.exception.notfound;

public abstract class BasicNotFoundException extends RuntimeException {
    public BasicNotFoundException(String message) {
        super(message);
    }
}
