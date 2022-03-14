package com.modsen.beershop.service.validator;

import com.modsen.beershop.controller.request.AddBeerRequest;

public class ContainerValidator implements Validator<AddBeerRequest> {

    public static final String BOTTLE = "bottle";
    public static final String DRAFT = "draft";
    public static final String INCORRECT_CONTAINER_MESSAGE = "Incorrect container!";

    @Override
    public boolean isValid(AddBeerRequest value) {
        return !(value.getContainer().equals(DRAFT) | value.getContainer().equals(BOTTLE));
    }

    @Override
    public String getErrorMessage() {
        return INCORRECT_CONTAINER_MESSAGE;
    }
}
