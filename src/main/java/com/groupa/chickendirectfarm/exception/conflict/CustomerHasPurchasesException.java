package com.groupa.chickendirectfarm.exception.conflict;

public class CustomerHasPurchasesException extends RuntimeException{
    public CustomerHasPurchasesException(String message){
        super(message);
    }
}
