package com.modsen.beershop.service.exception;

public class ValidateException extends RuntimeException{
    public ValidateException(String message) {
        super(message);
    }
}