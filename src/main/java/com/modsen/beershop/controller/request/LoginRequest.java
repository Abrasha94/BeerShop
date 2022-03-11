package com.modsen.beershop.controller.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
