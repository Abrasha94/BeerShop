package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.CreateBeerDto;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DraftBeerVerifier implements Verifier<CreateBeerRequest, CreateBeerDto> {

    public static final String DRAFT = "draft";

    private final List<Validator<Integer>> validators;

    @Override
    public CreateBeerDto verify(CreateBeerRequest createBeerRequest) {
        ValidateService.INSTANCE.validate(validators, createBeerRequest.getQuantity());
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
        return value.equals(DRAFT);
    }
}
