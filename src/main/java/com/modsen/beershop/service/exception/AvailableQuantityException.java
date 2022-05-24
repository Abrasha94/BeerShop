package com.modsen.beershop.service.exception;

public class AvailableQuantityException extends RuntimeException  {
    public AvailableQuantityException(String message) {
        super(message);
    }
}
