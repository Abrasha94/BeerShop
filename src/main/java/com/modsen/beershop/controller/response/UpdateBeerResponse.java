package com.modsen.beershop.controller.response;

import com.modsen.beershop.model.BeerDescription;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UpdateBeerResponse {
    private final Integer id;
    private final BeerDescription beerDescription;
}
