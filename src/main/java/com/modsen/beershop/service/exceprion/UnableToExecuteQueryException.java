package com.modsen.beershop.service.exceprion;

public class UnableToExecuteQueryException extends RuntimeException {
    public UnableToExecuteQueryException(String message) {
        super(message);
    }
}
