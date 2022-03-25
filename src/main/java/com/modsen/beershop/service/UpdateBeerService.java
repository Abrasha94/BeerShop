package com.modsen.beershop.service;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.request.UpdateBeerRequest;
import com.modsen.beershop.controller.response.UpdateBeerResponse;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exception.BeerNotFoundException;
import com.modsen.beershop.service.validator.Validator;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UpdateBeerService {

    private final List<Validator<UpdateBeerRequest>> validators;

    public UpdateBeerResponse update(UpdateBeerRequest updateBeerRequest) {
        final Integer id = updateBeerRequest.getId();
        final Integer quantity = updateBeerRequest.getQuantity();

        ValidateService.INSTANCE.validate(validators, updateBeerRequest);
        if (!BeerRepository.INSTANCE.isExistBeerById(id)) {
            throw new BeerNotFoundException(String.format(Messages.MESSAGE.beerWithIdNotFound(), id));
        }
        BeerRepository.INSTANCE.updateBeerTable(quantity, id);
        return UpdateBeerResponse.builder()
                .id(id)
                .quantity(quantity)
                .build();
    }
}
