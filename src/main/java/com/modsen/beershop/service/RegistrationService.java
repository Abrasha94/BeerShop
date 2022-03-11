package com.modsen.beershop.service;

import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.repository.UserRepository;
import com.modsen.beershop.service.exceprion.UserExistException;
import com.modsen.beershop.service.validator.Validator;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class RegistrationService {

    public static final String LOGIN_OR_EMAIL_TAKEN = "Login or Email is already taken";
    public static final String ROLE_USER = "user";

    private final ValidateService validateService;
    private final List<Validator<RegistrationRequest>> validators;
    private final UserRepository userRepository;

    public String register(RegistrationRequest registrationRequest) {

        final String login = registrationRequest.getLogin();
        final String password = registrationRequest.getPassword();
        final String email = registrationRequest.getEmail();

        validateService.validate(validators, registrationRequest);

        if (userRepository.isExistUserByLoginOrEmail(login, email)) {
            throw new UserExistException(LOGIN_OR_EMAIL_TAKEN);
        }

        final String uuid = UUID.randomUUID().toString();
        userRepository.createUser(login, password, email, uuid, ROLE_USER);

        return uuid;
    }
}
