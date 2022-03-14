package com.modsen.beershop.service.validator;

import com.modsen.beershop.controller.request.AddBeerRequest;
import com.modsen.beershop.repository.config.Configuration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IbuValidator implements Validator<AddBeerRequest> {
    public static final String INCORRECT_IBU_MESSAGE = "Incorrect IBU, please check!";

    private final Integer minIbu = Configuration.INSTANCE.getIbuMin();
    private final Integer maxIbu = Configuration.INSTANCE.getIbuMax();

    @Override
    public boolean isValid(AddBeerRequest value) {
        return (value.getIbu() <= minIbu) | (value.getIbu() >= maxIbu);
    }

    @Override
    public String getErrorMessage() {
        return INCORRECT_IBU_MESSAGE;
    }
}
