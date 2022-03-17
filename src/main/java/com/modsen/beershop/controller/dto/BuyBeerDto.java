package com.modsen.beershop.controller.dto;

import com.modsen.beershop.model.Beer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class BuyBeerDto {
    private final Beer beer;
    private Integer userId;
    private final Object quantity;
}
