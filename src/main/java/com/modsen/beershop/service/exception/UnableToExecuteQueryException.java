package com.modsen.beershop.service.exception;

public class UnableToExecuteQueryException extends RuntimeException {
    public UnableToExecuteQueryException(String message) {
        super(message);
    }
}
