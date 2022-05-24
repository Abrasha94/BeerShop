package com.modsen.beershop.service.validator;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class FieldValidator<T> implements Validator<T> {

    private final Function<T, ?> function;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return function.apply(value) == null || function.apply(value).toString().isEmpty();
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
