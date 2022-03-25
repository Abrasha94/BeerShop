package com.modsen.beershop.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class Beer {
    private Integer id;
    private String name;
    private String container;
    private String type;
    private Double abv;
    private Integer ibu;
    private Instant created;
    private Instant update;
    private String beerDescription;
    private Integer quantity;
}
