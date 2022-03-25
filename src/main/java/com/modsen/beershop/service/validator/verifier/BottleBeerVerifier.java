package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.CreateBeerDto;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BottleBeerVerifier implements Verifier<CreateBeerRequest, CreateBeerDto> {

    public static final String BOTTLE = "bottle";

    private final List<Validator<CreateBeerRequest>> validators;

    @Override
    public CreateBeerDto verify(CreateBeerRequest createBeerRequest) {
        ValidateService.INSTANCE.validate(validators, createBeerRequest);
        return CreateBeerDto.builder()
                .name(createBeerRequest.getName())
                .container(createBeerRequest.getContainer())
                .type(createBeerRequest.getType())
                .abv(createBeerRequest.getAbv())
                .ibu(createBeerRequest.getIbu())
                .beerDescription(createBeerRequest.getBeerDescription())
                .quantity(createBeerRequest.getQuantity())
                .build();
    }

    @Override
    public boolean filter(Object value) {
        return value.equals(BOTTLE);
    }
}
