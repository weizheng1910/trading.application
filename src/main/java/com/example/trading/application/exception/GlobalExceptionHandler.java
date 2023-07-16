package com.example.trading.application.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleException(InsufficientBalanceException e){
        return e.getMessage();
    }

    @ExceptionHandler
    public String handleException(BalanceNotFoundException e){
        return e.getMessage();
    }

    @ExceptionHandler
    public String handleException(ConstraintViolationException e){
        var msg= e.getMessage().split(":");
        return msg[1];
    }

    @ExceptionHandler
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }


}
