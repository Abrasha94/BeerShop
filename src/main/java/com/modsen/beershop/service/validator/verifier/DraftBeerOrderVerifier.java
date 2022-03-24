package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.model.DraftBeerOrder;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exception.AvailableQuantityException;
import com.modsen.beershop.service.exception.BeerNotFoundException;

import java.util.ArrayList;
import java.util.List;


public class DraftBeerOrderVerifier implements Verifier<BuyBeerRequest, List<BuyBeerDto>> {
    @Override
    public List<BuyBeerDto> verify(BuyBeerRequest value) {
        List<BuyBeerDto> buyBeerDtoList = new ArrayList<>();
        for (DraftBeerOrder draftBeerOrder : value.getDraftBeerOrders()) {
            final Beer beer =
                    BeerRepository.INSTANCE.readBeerById(draftBeerOrder.getId(), DraftBeerDescription.class)
                            .orElseThrow(() -> new BeerNotFoundException(Messages.MESSAGE.beerNotFound()));
            final DraftBeerDescription beerDescription = (DraftBeerDescription) beer.getBeerDescription();
            final Double availability = beerDescription.getQuantity();
            if (availability < draftBeerOrder.getQuantity()) {
                throw new AvailableQuantityException(Messages.MESSAGE.notEnoughQuantity());
            }
            beerDescription.setQuantity(availability - draftBeerOrder.getQuantity());
            beer.setBeerDescription(beerDescription);
            buyBeerDtoList.add(BuyBeerDto.builder()
                    .beer(beer)
                    .quantity(draftBeerOrder.getQuantity())
                    .build());
        }
        return buyBeerDtoList;
    }

    @Override
    public boolean filter(Object value) {
        return ((BuyBeerRequest) value).getDraftBeerOrders() != null;
    }
}
