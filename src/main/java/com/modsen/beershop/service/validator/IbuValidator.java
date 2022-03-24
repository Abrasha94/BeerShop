package com.modsen.beershop.service.validator;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.config.Configuration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IbuValidator implements Validator<CreateBeerRequest> {

    private final Integer minIbu = Configuration.INSTANCE.getIbuMin();
    private final Integer maxIbu = Configuration.INSTANCE.getIbuMax();

    @Override
    public boolean isValid(CreateBeerRequest value) {
        return (value.getIbu() <= minIbu) | (value.getIbu() >= maxIbu);
    }

    @Override
    public String getErrorMessage() {
        return Messages.MESSAGE.incorrectIbu();
    }
}
