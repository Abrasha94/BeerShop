package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BottleBeerVerifier implements Verifier<CreateBeerRequest, AddBeerDto> {

    public static final String BOTTLE = "bottle";

    private final List<Validator<BottleBeerDescription>> validators;

    @Override
    public AddBeerDto verify(CreateBeerRequest createBeerRequest) {
        final BottleBeerDescription bottleBeerDescription = (BottleBeerDescription) createBeerRequest.getBeerDescription();
        ValidateService.INSTANCE.validate(validators, bottleBeerDescription);
        return AddBeerDto.builder()
                .name(createBeerRequest.getName())
                .container(createBeerRequest.getContainer())
                .type(createBeerRequest.getType())
                .abv(createBeerRequest.getAbv())
                .ibu(createBeerRequest.getIbu())
                .beerDescription(bottleBeerDescription)
                .build();
    }

    @Override
    public boolean filter(Object value) {
        return value.equals(BOTTLE);
    }
}
