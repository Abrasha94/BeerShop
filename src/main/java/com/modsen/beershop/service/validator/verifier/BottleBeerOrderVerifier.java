package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.model.BottleBeerOrder;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.exceprion.AvailableQuantityException;
import com.modsen.beershop.service.exceprion.BeerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BottleBeerOrderVerifier implements Verifier<BuyBeerRequest, List<BuyBeerDto>> {

    public static final String BEER_NOT_FOUND = "Beer not found!";
    public static final String NOT_ENOUGH_QUANTITY = "Not enough quantity!";

    @Override
    public List<BuyBeerDto> verify(BuyBeerRequest value) {
        List<BuyBeerDto> buyBeerDtoList = new ArrayList<>();
        for (BottleBeerOrder bottleBeerOrder : value.getBottleBeerOrders()) {
            final Beer beer =
                    BeerRepository.INSTANCE.readBeerById(bottleBeerOrder.getId(), BottleBeerDescription.class)
                            .orElseThrow(() -> new BeerNotFoundException(BEER_NOT_FOUND));
            final BottleBeerDescription beerDescription = (BottleBeerDescription) beer.getBeerDescription();
            final Integer availability = beerDescription.getQuantity();
            if (availability < bottleBeerOrder.getQuantity()) {
                throw new AvailableQuantityException(NOT_ENOUGH_QUANTITY);
            }
            beerDescription.setQuantity(availability - bottleBeerOrder.getQuantity());
            beer.setBeerDescription(beerDescription);
            buyBeerDtoList.add(BuyBeerDto.builder()
                    .beer(beer)
                    .quantity(bottleBeerOrder.getQuantity())
                    .build());
        }
        return buyBeerDtoList;
    }

    @Override
    public boolean filter(Object value) {
        return ((BuyBeerRequest) value).getBottleBeerOrders() != null;
    }
}
