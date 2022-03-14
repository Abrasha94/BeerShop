package com.modsen.beershop.service.validator;

import com.modsen.beershop.repository.config.Configuration;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class ContainerVolumeValidator<T> implements Validator<T> {

    private final Function<T, Double> function;
    private final String message;

    @Override
    public boolean isValid(T value) {
        return !(function.apply(value) > Configuration.INSTANCE.getContainerMin()
                & function.apply(value) <= Configuration.INSTANCE.getContainerMax());
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
