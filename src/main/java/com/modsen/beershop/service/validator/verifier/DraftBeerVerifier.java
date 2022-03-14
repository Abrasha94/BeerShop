package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.controller.request.AddBeerRequest;
import com.modsen.beershop.model.BeerDescription;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DraftBeerVerifier implements Verifier<AddBeerRequest, AddBeerDto> {

    public static final String DRAFT = "draft";

    private final List<Validator<DraftBeerDescription>> validators;

    @Override
    public AddBeerDto verify(AddBeerRequest addBeerRequest) {
        final DraftBeerDescription draftBeerDescription = (DraftBeerDescription) addBeerRequest.getBeerDescription();
        ValidateService.INSTANCE.validate(validators, draftBeerDescription);
        return AddBeerDto.builder()
                .name(addBeerRequest.getName())
                .container(addBeerRequest.getContainer())
                .type(addBeerRequest.getType())
                .abv(addBeerRequest.getAbv())
                .ibu(addBeerRequest.getIbu())
                .beerDescription(draftBeerDescription)
                .build();

    }

    @Override
    public boolean filter(Object value) {
        return value.equals(DRAFT);
    }
}
