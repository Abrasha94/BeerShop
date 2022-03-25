package com.modsen.beershop.service.validator.verifier;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.model.Beer;
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
                    BeerRepository.INSTANCE.readBeerById(draftBeerOrder.getId())
                            .orElseThrow(() -> new BeerNotFoundException(Messages.MESSAGE.beerNotFound()));
            final Integer availability = beer.getQuantity();
            if (availability < draftBeerOrder.getQuantity()) {
                throw new AvailableQuantityException(Messages.MESSAGE.notEnoughQuantity());
            }
            beer.setQuantity(availability - draftBeerOrder.getQuantity());
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
