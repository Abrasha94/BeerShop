package com.modsen.beershop.controller.request;

import com.modsen.beershop.model.BeerDescription;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateBeerRequest {
    private String name;
    private String container;
    private String type;
    private Double abv;
    private Integer ibu;
    private BeerDescription beerDescription;
}
