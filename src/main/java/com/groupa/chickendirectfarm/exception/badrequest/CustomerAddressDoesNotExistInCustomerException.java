package com.groupa.chickendirectfarm.exception.badrequest;

public class CustomerAddressDoesNotExistInCustomerException extends RuntimeException {
    public CustomerAddressDoesNotExistInCustomerException(String message) {
        super(message);
    }
}
