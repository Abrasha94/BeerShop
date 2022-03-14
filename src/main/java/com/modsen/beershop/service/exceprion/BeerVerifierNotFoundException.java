package com.modsen.beershop.service.exceprion;

public class BeerVerifierNotFoundException extends RuntimeException {
    public BeerVerifierNotFoundException(String message) {
        super(message);
    }
}
