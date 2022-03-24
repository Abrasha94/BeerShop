package com.modsen.beershop.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
public class User {
    private String role;
    private String login;
    private String pass;
    private String email;
    private UUID uuid;
}
