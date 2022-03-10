package com.modsen.beershop.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class UserTransaction {
    private Integer id;
    private Integer userId;
    private Integer beerId;
    private Instant timeOfSale;
}
