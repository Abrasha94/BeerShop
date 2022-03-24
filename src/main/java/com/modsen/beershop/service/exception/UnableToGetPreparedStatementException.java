package com.modsen.beershop.service.exception;

public class UnableToGetPreparedStatementException extends RuntimeException {
    public UnableToGetPreparedStatementException(String message) {
        super(message);
    }
}
