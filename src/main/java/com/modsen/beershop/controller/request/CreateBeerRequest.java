package com.modsen.beershop.controller.request;

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
    private String beerDescription;
    private Integer quantity;
    private Double containerVolume;
}
