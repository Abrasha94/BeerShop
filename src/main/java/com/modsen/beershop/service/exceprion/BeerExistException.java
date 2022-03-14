package com.modsen.beershop.service.exceprion;

public class BeerExistException extends RuntimeException {
    public BeerExistException(String message) {
        super(message);
    }
}
