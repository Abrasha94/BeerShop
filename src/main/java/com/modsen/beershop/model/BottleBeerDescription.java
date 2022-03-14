package com.modsen.beershop.model;

import lombok.Data;

@Data
public class BottleBeerDescription implements BeerDescription{
    private Double containerVolume;
    private Integer quantity;
}
