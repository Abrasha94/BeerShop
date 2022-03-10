package com.modsen.beershop.service.exceprion;

public class UnableToExecuteQuery extends RuntimeException {
    public UnableToExecuteQuery(String message) {
        super(message);
    }
}
