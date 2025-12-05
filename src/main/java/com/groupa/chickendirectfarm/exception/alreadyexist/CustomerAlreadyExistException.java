package com.groupa.chickendirectfarm.exception.alreadyexist;

public class CustomerAlreadyExistException extends RuntimeException{
    public CustomerAlreadyExistException(String message) {
        super(message);
    }
}
