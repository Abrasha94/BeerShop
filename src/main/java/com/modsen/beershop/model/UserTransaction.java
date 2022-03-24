package com.modsen.beershop.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class UserTransaction {
    private UUID userUuid;
    private Integer beerId;
    private Instant timeOfSale;

    private Object quantity;
}
