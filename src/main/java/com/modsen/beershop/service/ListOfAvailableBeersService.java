package com.modsen.beershop.service;

import com.modsen.beershop.controller.response.ListOfAvailableBeersResponse;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.repository.BeerRepository;
import com.modsen.beershop.service.validator.PageValidator;

import java.util.List;

public enum ListOfAvailableBeersService {
    INSTANCE;

    public ListOfAvailableBeersResponse read(Integer page, Integer size) {
        PageValidator.INSTANCE.validatePage(page);
        final Integer pageSize = PageValidator.INSTANCE.validatePageSize(size);
        final List<Beer> beers = BeerRepository.INSTANCE.readActiveBeers(pageSize, page);
        return new ListOfAvailableBeersResponse(beers);
    }
}
