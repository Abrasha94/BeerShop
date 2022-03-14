package com.modsen.beershop.service.validator.verifier;

public interface Verifier<T, K> {
    K verify(T value);

    boolean filter(Object value);
}
