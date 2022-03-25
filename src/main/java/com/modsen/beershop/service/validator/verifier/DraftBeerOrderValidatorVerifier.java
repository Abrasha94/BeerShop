package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.model.DraftBeerOrder;
import com.modsen.beershop.service.validator.Validator;
import com.modsen.beershop.service.validator.verifier.Verifier;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class DraftBeerOrderValidatorVerifier implements Verifier<BuyBeerRequest, String> {
    private final List<Validator<DraftBeerOrder>> validators;

    @Override
    public String verify(BuyBeerRequest value) {
        return value.getDraftBeerOrders().stream()
                .map(b -> {
                    final Optional<String> s = validators.stream()
                            .filter(v -> v.isValid(b))
                            .findFirst()
                            .map(Validator::getErrorMessage);
                    return s.orElse(null);
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean filter(Object value) {
        return ((BuyBeerRequest) value).getDraftBeerOrders().size() != 0;
    }
}

