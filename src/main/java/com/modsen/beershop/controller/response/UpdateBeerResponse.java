package com.modsen.beershop.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UpdateBeerResponse {
    private final Integer id;
    private final Integer quantity;
}
