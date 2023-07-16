package com.example.trading.application.exception;

public class BalanceNotFoundException extends RuntimeException{
    public BalanceNotFoundException(String msg){
        super(msg);
    }
}
