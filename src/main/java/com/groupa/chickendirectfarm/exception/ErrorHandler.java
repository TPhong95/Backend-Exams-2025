package com.groupa.chickendirectfarm.exception;

import com.groupa.chickendirectfarm.exception.alreadyexist.CustomerAddressAlreadyExistException;
import com.groupa.chickendirectfarm.exception.alreadyexist.CustomerAlreadyExistException;
import com.groupa.chickendirectfarm.exception.alreadyexist.ProductAlreadyExistsException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerAddressNotFoundException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
import com.groupa.chickendirectfarm.exception.notfound.ProductNotFoundException;
import com.groupa.chickendirectfarm.exception.notfound.PurchaseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
@Slf4j
public class ErrorHandler {


    @ExceptionHandler(OutOfStockException.class)
     public ResponseEntity<String> handleOutOfStockException(OutOfStockException e) {
        log.warn(e.getMessage());
         return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
     }

     @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException e) {
         log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAddressNotFoundException.class)
    public ResponseEntity<String> handleCustomerAddressNotFoundException(CustomerAddressNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<String> handlePurchaseNotFoundException(PurchaseNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<String> handleCustomerAlreadyExistException(CustomerAlreadyExistException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerAddressAlreadyExistException.class)
    public ResponseEntity<String>  handleCustomerAddressAlreadyExistException(CustomerAddressAlreadyExistException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExistException(ProductAlreadyExistsException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateProductInPurchaseException.class)
    public ResponseEntity<String> handleDuplicateProductInPurchaseException(DuplicateProductInPurchaseException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerHasPurchasesException.class)
    public ResponseEntity<String> handleCustomerHasPurchasesException(CustomerHasPurchasesException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
