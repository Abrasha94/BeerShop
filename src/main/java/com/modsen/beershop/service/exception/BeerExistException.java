package com.modsen.beershop.service.exception;

public class BeerExistException extends RuntimeException {
    public BeerExistException(String message) {
        super(message);
    }
}
