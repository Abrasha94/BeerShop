package com.modsen.beershop.service.validator;

import com.modsen.beershop.controller.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator<RegistrationRequest> {

    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@" +
            "[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";
    public static final String INCORRECT_EMAIL_MESSAGE = "Incorrect email!";

    @Override
    public boolean isValid(RegistrationRequest value) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(value.getEmail());
        return matcher.matches();
    }

    @Override
    public String getErrorMessage() {
        return INCORRECT_EMAIL_MESSAGE;
    }
}
