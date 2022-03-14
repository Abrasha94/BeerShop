package com.modsen.beershop.controller.dto;

import com.modsen.beershop.model.BeerDescription;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AddBeerDto {
    private Integer id;
    private final String name;
    private final String container;
    private final String type;
    private final Double abv;
    private final Integer ibu;
    private final BeerDescription beerDescription;
}
