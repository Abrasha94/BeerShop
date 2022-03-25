package com.modsen.beershop.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CreateBeerDto {
    private Integer id;
    private final String name;
    private final String container;
    private final String type;
    private final Double abv;
    private final Integer ibu;
    private final String beerDescription;
    private final Integer quantity;
}
