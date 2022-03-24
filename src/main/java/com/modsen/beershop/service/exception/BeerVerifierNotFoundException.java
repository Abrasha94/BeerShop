package com.modsen.beershop.service.exception;

public class BeerVerifierNotFoundException extends RuntimeException {
    public BeerVerifierNotFoundException(String message) {
        super(message);
    }
}
