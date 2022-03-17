package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.model.BottleBeerOrder;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.model.DraftBeerOrder;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exceprion.AvailableQuantityException;
import com.modsen.beershop.service.exceprion.BeerNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.modsen.beershop.service.validator.verifier.BottleBeerOrderVerifier.BEER_NOT_FOUND;
import static com.modsen.beershop.service.validator.verifier.BottleBeerOrderVerifier.NOT_ENOUGH_QUANTITY;

public class DraftBeerOrderVerifier implements Verifier<BuyBeerRequest, List<BuyBeerDto>> {
    @Override
    public List<BuyBeerDto> verify(BuyBeerRequest value) {
        List<BuyBeerDto> buyBeerDtoList = new ArrayList<>();
        for (DraftBeerOrder draftBeerOrder : value.getDraftBeerOrders()) {
            final Beer beer =
                    BeerRepository.INSTANCE.readBeerById(draftBeerOrder.getId(), DraftBeerDescription.class)
                            .orElseThrow(() -> new BeerNotFoundException(BEER_NOT_FOUND));
            final DraftBeerDescription beerDescription = (DraftBeerDescription) beer.getBeerDescription();
            final Double availability = beerDescription.getQuantity();
            if (availability < draftBeerOrder.getQuantity()) {
                throw new AvailableQuantityException(NOT_ENOUGH_QUANTITY);
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
