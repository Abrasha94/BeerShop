package com.modsen.beershop.service;

import com.modsen.beershop.controller.request.UpdateBeerRequest;
import com.modsen.beershop.controller.response.UpdateBeerResponse;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exceprion.BeerNotFoundException;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UpdateBeerService {

    public static final String BEER_NOT_FOUND = "Beer with id = %d not found";
    private final List<Validator<UpdateBeerRequest>> validators;

    public UpdateBeerResponse update(UpdateBeerRequest updateBeerRequest) {
        final Integer id = updateBeerRequest.getId();
        final Double containerVolume = updateBeerRequest.getContainerVolume();
        final Integer quantity = updateBeerRequest.getQuantity();
        final BottleBeerDescription beerDescription = new BottleBeerDescription(containerVolume, quantity);

        ValidateService.INSTANCE.validate(validators, updateBeerRequest);
        if (!BeerRepository.INSTANCE.isExistBeerById(id)) {
            throw new BeerNotFoundException(String.format(BEER_NOT_FOUND, id));
        }
        BeerRepository.INSTANCE.updateBeerDescription(beerDescription, id);
        return UpdateBeerResponse.builder()
                .id(id)
                .beerDescription(beerDescription)
                .build();
    }
}
