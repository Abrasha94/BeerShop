package com.modsen.beershop.controller.response;

import com.modsen.beershop.model.Beer;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ListOfAvailableBeersResponse {
    private final List<Beer> beers;
}
