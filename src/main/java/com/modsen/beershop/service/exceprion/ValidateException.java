package com.modsen.beershop.service.exceprion;

public class ValidateException extends RuntimeException{
    public ValidateException(String message) {
        super(message);
    }
}
