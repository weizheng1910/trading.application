package com.example.trading.application.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String msg){
        super(msg);
    }
}
