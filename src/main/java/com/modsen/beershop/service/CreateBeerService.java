package com.modsen.beershop.service;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.dto.CreateBeerDto;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.controller.response.CreateBeerResponse;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exception.BeerExistException;
import com.modsen.beershop.service.exception.BeerVerifierNotFoundException;
import com.modsen.beershop.service.validator.Validator;
import com.modsen.beershop.service.validator.verifier.Verifier;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateBeerService {

    private final List<Validator<CreateBeerRequest>> validators;
    private final List<Verifier<CreateBeerRequest, CreateBeerDto>> verifiers;

    public CreateBeerResponse create(CreateBeerRequest createBeerRequest) {
        ValidateService.INSTANCE.validate(validators, createBeerRequest);
        if (BeerRepository.INSTANCE.isExistBeerByNameAndContainer(createBeerRequest.getName(), createBeerRequest.getContainer())) {
            throw new BeerExistException(Messages.MESSAGE.beerAlreadyExist());
        }
        final CreateBeerDto createBeerDto = BeerRepository.INSTANCE.createBeer(verifiers.stream()
                .filter(v -> v.filter(createBeerRequest.getContainer()))
                .findFirst()
                .map(v -> v.verify(createBeerRequest))
                .orElseThrow(() -> new BeerVerifierNotFoundException(Messages.MESSAGE.verifierNotFound())));

        return CreateBeerResponse.builder()
                .id(createBeerDto.getId())
                .name(createBeerDto.getName())
                .container(createBeerDto.getContainer())
                .type(createBeerDto.getType())
                .abv(createBeerDto.getAbv())
                .ibu(createBeerDto.getIbu())
                .beerDescription(createBeerDto.getBeerDescription())
                .build();
    }
}
