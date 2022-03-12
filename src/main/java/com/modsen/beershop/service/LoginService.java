package com.modsen.beershop.service;

import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.model.User;
import com.modsen.beershop.repository.UserRepository;
import com.modsen.beershop.service.exceprion.UserNotFoundException;
import com.modsen.beershop.service.validator.Validator;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LoginService {
    public static final String INCORRECT_LOGIN_OR_PASSWORD = "Wrong login or password!";
    private final ValidateService validateService;
    private final UserRepository userRepository;
    private final List<Validator<LoginRequest>> validators;

    public User login(LoginRequest loginRequest) {

        validateService.validate(validators, loginRequest);
        return userRepository.readUserByLoginAndPassword(loginRequest.getLogin(), loginRequest.getPassword())
                .orElseThrow(() -> new UserNotFoundException(INCORRECT_LOGIN_OR_PASSWORD));
    }
}
