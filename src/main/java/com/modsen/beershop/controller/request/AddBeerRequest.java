package com.modsen.beershop.controller.request;

import com.modsen.beershop.model.BeerDescription;
import lombok.Data;

@Data
public class AddBeerRequest {
    private final String name;
    private final String container;
    private final String type;
    private final Double abv;
    private final Integer ibu;
    private final BeerDescription beerDescription;
}
