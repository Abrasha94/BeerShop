package com.modsen.beershop.controller.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class RegistrationResponse {
    private final UUID uuid;
}
