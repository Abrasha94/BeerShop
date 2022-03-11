package com.modsen.beershop.service.validator;

public interface Validator<T> {
    boolean isValid(T value);

    String getErrorMessage();
}
