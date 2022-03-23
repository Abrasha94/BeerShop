package com.modsen.beershop.service;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.controller.response.AddBeerResponse;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exceprion.BeerExistException;
import com.modsen.beershop.service.exceprion.BeerVerifierNotFoundException;
import com.modsen.beershop.service.validator.Validator;
import com.modsen.beershop.service.validator.verifier.Verifier;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateBeerService {

    public static final String BEER_EXIST_MESSAGE = "Beer already exist!";
    public static final String VERIFIER_NOT_FOUND_MESSAGE = "Verifier for this beer not found!";

    private final List<Validator<CreateBeerRequest>> validators;
    private final List<Verifier<CreateBeerRequest, AddBeerDto>> verifiers;

    public AddBeerResponse create(CreateBeerRequest createBeerRequest) {
        ValidateService.INSTANCE.validate(validators, createBeerRequest);
        if (BeerRepository.INSTANCE.isExistBeerByNameAndContainer(createBeerRequest.getName(), createBeerRequest.getContainer())) {
            throw new BeerExistException(BEER_EXIST_MESSAGE);
        }
        final AddBeerDto addBeerDto = BeerRepository.INSTANCE.createBeer(verifiers.stream()
                .filter(v -> v.filter(createBeerRequest.getContainer()))
                .findFirst()
                .map(v -> v.verify(createBeerRequest))
                .orElseThrow(() -> new BeerVerifierNotFoundException(VERIFIER_NOT_FOUND_MESSAGE)));

        return AddBeerResponse.builder()
                .id(addBeerDto.getId())
                .name(addBeerDto.getName())
                .container(addBeerDto.getContainer())
                .type(addBeerDto.getType())
                .abv(addBeerDto.getAbv())
                .ibu(addBeerDto.getIbu())
                .beerDescription(addBeerDto.getBeerDescription())
                .build();
    }
}