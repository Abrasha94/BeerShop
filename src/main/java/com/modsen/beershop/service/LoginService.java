package com.modsen.beershop.service;

import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.model.User;
import com.modsen.beershop.repository.UserRepository;
import com.modsen.beershop.service.validator.Validator;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LoginService {
    private final List<Validator<LoginRequest>> validators;

    public User login(LoginRequest loginRequest) {

        ValidateService.INSTANCE.validate(validators, loginRequest);
        return UserRepository.INSTANCE.readUserByLoginAndPassword(loginRequest.getLogin(), loginRequest.getPassword());
    }
}
