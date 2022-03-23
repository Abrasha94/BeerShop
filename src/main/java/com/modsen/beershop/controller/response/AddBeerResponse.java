package com.modsen.beershop.controller.response;

import com.modsen.beershop.model.BeerDescription;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AddBeerResponse {
    private final Integer id;
    private final String name;
    private final String container;
    private final String type;
    private final Double abv;
    private final Integer ibu;
    private final BeerDescription beerDescription;
}