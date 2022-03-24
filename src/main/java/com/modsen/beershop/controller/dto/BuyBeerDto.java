package com.modsen.beershop.controller.dto;

import com.modsen.beershop.model.Beer;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class BuyBeerDto {
    private final Beer beer;
    private UUID userUuid;
    private final Object quantity;
}
