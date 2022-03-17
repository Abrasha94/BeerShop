package com.modsen.beershop.controller.request;

import com.modsen.beershop.model.BottleBeerOrder;
import com.modsen.beershop.model.DraftBeerOrder;
import lombok.Data;

import java.util.List;

@Data
public class BuyBeerRequest {
    private final List<DraftBeerOrder> draftBeerOrders;
    private final List<BottleBeerOrder> bottleBeerOrders;
}
