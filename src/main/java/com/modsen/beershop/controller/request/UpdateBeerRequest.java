package com.modsen.beershop.controller.request;

import lombok.Data;

@Data
public class UpdateBeerRequest {
    private final Integer id;
    private final Double containerVolume;
    private final Integer quantity;
}
