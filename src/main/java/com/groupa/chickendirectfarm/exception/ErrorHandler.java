package com.groupa.chickendirectfarm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {



    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> handleCustomerAddressNotFoundException(CustomerAddressNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> handlePurchaseNotFoundException(PurchaseNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
