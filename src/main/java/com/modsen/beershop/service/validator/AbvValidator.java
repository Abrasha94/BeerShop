package com.modsen.beershop.service.validator;

import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.repository.config.Configuration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbvValidator implements Validator<CreateBeerRequest> {
    public static final String INCORRECT_ABV_MESSAGE = "Incorrect ABV, please check!";
    private final Double minAbv = Configuration.INSTANCE.getAbvMin();
    private final Double maxAbv = Configuration.INSTANCE.getAbvMax();

    @Override
    public boolean isValid(CreateBeerRequest value) {
        return (value.getAbv() <= minAbv) | (value.getAbv() >= maxAbv);
    }

    @Override
    public String getErrorMessage() {
        return INCORRECT_ABV_MESSAGE;
    }
}