package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.controller.request.AddBeerRequest;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BottleBeerVerifier implements Verifier<AddBeerRequest, AddBeerDto> {

    public static final String BOTTLE = "bottle";

    private final List<Validator<BottleBeerDescription>> validators;

    @Override
    public AddBeerDto verify(AddBeerRequest addBeerRequest) {
        final BottleBeerDescription bottleBeerDescription = (BottleBeerDescription) addBeerRequest.getBeerDescription();
        ValidateService.INSTANCE.validate(validators, bottleBeerDescription);
        return AddBeerDto.builder()
                .name(addBeerRequest.getName())
                .container(addBeerRequest.getContainer())
                .type(addBeerRequest.getType())
                .abv(addBeerRequest.getAbv())
                .ibu(addBeerRequest.getIbu())
                .beerDescription(bottleBeerDescription)
                .build();
    }

    @Override
    public boolean filter(Object value) {
        return value.equals(BOTTLE);
    }
}
