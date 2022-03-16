package com.modsen.beershop.service.exceprion;

public class BeerNotFoundException extends RuntimeException {
    public BeerNotFoundException(String message) {
        super(message);
    }
}
