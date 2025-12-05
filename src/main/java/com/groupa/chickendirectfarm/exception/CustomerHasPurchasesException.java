package com.groupa.chickendirectfarm.exception;

public class CustomerHasPurchasesException extends RuntimeException{
    public CustomerHasPurchasesException(String message){
        super(message);
    }
}
