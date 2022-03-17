package com.modsen.beershop.service.exceprion;

public class AvailableQuantityException extends RuntimeException {
    public AvailableQuantityException(String message) {
        super(message);
    }
}
