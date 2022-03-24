package com.modsen.beershop.service.validator;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.request.CreateBeerRequest;

public class ContainerValidator implements Validator<CreateBeerRequest> {

    public static final String BOTTLE = "bottle";
    public static final String DRAFT = "draft";

    @Override
    public boolean isValid(CreateBeerRequest value) {
        return !(value.getContainer().equals(DRAFT) | value.getContainer().equals(BOTTLE));
    }

    @Override
    public String getErrorMessage() {
        return Messages.MESSAGE.incorrectContainer();
    }
}
