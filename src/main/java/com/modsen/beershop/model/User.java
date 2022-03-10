package com.modsen.beershop.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class User {
    private Integer id;
    private String role;
    private String login;
    private String pass;
    private String email;
    private String uuid;
}
