package com.modsen.beershop.controller.request;

import lombok.Data;

@Data
public class UpdateBeerRequest {
    private final Integer id;
    private final Integer quantity;
}
