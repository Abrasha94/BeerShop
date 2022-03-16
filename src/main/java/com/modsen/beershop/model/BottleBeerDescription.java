package com.modsen.beershop.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BottleBeerDescription implements BeerDescription{
    private Double containerVolume;
    private Integer quantity;
}
