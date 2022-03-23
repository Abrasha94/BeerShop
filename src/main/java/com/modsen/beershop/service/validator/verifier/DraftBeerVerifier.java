package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DraftBeerVerifier implements Verifier<CreateBeerRequest, AddBeerDto> {

    public static final String DRAFT = "draft";

    private final List<Validator<DraftBeerDescription>> validators;

    @Override
    public AddBeerDto verify(CreateBeerRequest createBeerRequest) {
        final DraftBeerDescription draftBeerDescription = (DraftBeerDescription) createBeerRequest.getBeerDescription();
        ValidateService.INSTANCE.validate(validators, draftBeerDescription);
        return AddBeerDto.builder()
                .name(createBeerRequest.getName())
                .container(createBeerRequest.getContainer())
                .type(createBeerRequest.getType())
                .abv(createBeerRequest.getAbv())
                .ibu(createBeerRequest.getIbu())
                .beerDescription(draftBeerDescription)
                .build();

    }

    @Override
    public boolean filter(Object value) {
        return value.equals(DRAFT);
    }
}
