package com.modsen.beershop.service;

import com.modsen.beershop.service.exceprion.ValidateException;
import com.modsen.beershop.service.validator.Validator;

import java.util.List;
import java.util.Optional;

public class ValidateService {

    public <T> void validate(List<Validator<T>> validators, T data) {
        final Optional<String> errorMessage = validators.stream()
                .filter(tValidator -> tValidator.isValid(data))
                .findFirst()
                .map(Validator::getErrorMessage);
        if (errorMessage.isPresent()) {
            throw new ValidateException(errorMessage.get());
        }
    }
}