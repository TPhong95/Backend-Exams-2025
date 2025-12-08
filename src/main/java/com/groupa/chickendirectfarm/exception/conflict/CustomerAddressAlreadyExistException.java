package com.groupa.chickendirectfarm.exception.conflict;

public class CustomerAddressAlreadyExistException extends RuntimeException {
    public CustomerAddressAlreadyExistException(String message) {
        super(message);
    }
}
