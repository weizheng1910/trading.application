package com.example.trading.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler
//    public ResponseEntity handleException(MethodArgumentNotValidException e){
//
//        String message = e.getMessage();
//        BindingResult result = e.getBindingResult();
//
//        String propertyName = result.getFieldError().getField();
//        String errorMessage = result.getFieldError().getDefaultMessage();
//        String responseBody = String.format("Field with error: %1$s, Error message: %2$s", propertyName, errorMessage);
//
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(responseBody);
//
//    }

    @ExceptionHandler
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }


}
