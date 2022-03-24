package com.modsen.beershop.service.validator;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidator implements Validator<RegistrationRequest> {

    public static final String NAME_REGEX = "^[A-Za-z]";

    @Override
    public boolean isValid(RegistrationRequest value) {
        final Pattern pattern = Pattern.compile(NAME_REGEX);
        final Matcher matcher = pattern.matcher(value.getLogin());
        return !matcher.matches();
    }

    @Override
    public String getErrorMessage() {
        return Messages.MESSAGE.invalidLogin();
    }
}
