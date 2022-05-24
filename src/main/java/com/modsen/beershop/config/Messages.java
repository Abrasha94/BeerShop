package com.modsen.beershop.config;

import com.modsen.beershop.service.exception.MessageException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public enum Messages {
    MESSAGE;

    public static final String PROPERTIES_FILE = "src/main/resources/messages.properties";
    public static final String BEER_NOT_FOUND = "beer.not.found";
    public static final String NOT_ENOUGH_QUANTITY = "not.enough.quantity";
    public static final String INCORRECT_ABV_MESSAGE = "incorrect.abv.message";
    public static final String INCORRECT_CONTAINER = "incorrect.container";
    public static final String INCORRECT_EMAIL = "incorrect.email";
    public static final String INCORRECT_IBU = "incorrect.ibu";
    public static final String INVALID_LOGIN = "invalid.login";
    public static final String INVALID_PAGE = "invalid.page";
    public static final String BEER_ALREADY_EXIST = "beer.already.exist";
    public static final String VERIFIER_NOT_FOUND = "verifier.not.found";
    public static final String WRONG_LOGIN_OR_PASSWORD = "wrong.login.or.password";
    public static final String LOGIN_OR_EMAIL_TAKEN = "login.or.email.taken";
    public static final String BEER_WITH_ID_NOT_FOUND = "beer.with.id.not.found";
    public static final String LOGIN_EMPTY = "login.empty";
    public static final String PASSWORD_EMPTY = "password.empty";
    public static final String EMAIL_EMPTY = "email.empty";
    public static final String COMMAND_NOT_FOUND = "command.not.found";
    public static final String NAME_EMPTY = "name.empty";
    public static final String CONTAINER_EMPTY = "container.empty";
    public static final String TYPE_EMPTY = "type.empty";
    public static final String ABV_EMPTY = "abv.empty";
    public static final String IBU_EMPTY = "ibu.empty";
    public static final String QUANTITY_SMALL = "quantity.small";
    public static final String VOLUME_SMALL = "volume.small";
    public static final String BOTTLE_QUANTITY_SMALL = "bottle.quantity.small";
    public static final String INCORRECT_VOLUME = "incorrect.volume";
    public static final String BEER_ID_EMPTY = "beer.id.empty";
    public static final String VOLUME_EMPTY = "volume.empty";
    public static final String QUANTITY_EMPTY = "quantity.empty";
    public static final String ID_NULL = "id.null";
    public static final String QUANTITY_NULL = "quantity.null";

    private final Properties properties = new Properties();

    Messages() {
        try (FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE)){
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new MessageException(e.getMessage());
        }
    }

    public String beerNotFound() {
        return properties.getProperty(BEER_NOT_FOUND);
    }

    public String notEnoughQuantity() {
        return properties.getProperty(NOT_ENOUGH_QUANTITY);
    }

    public String incorrectAbvMessage() {
        return properties.getProperty(INCORRECT_ABV_MESSAGE);
    }

    public String incorrectContainer() {
        return properties.getProperty(INCORRECT_CONTAINER);
    }

    public String incorrectEmail() {
        return properties.getProperty(INCORRECT_EMAIL);
    }

    public String incorrectIbu() {
        return properties.getProperty(INCORRECT_IBU);
    }

    public String invalidLogin() {
        return properties.getProperty(INVALID_LOGIN);
    }

    public String invalidPage() {
        return properties.getProperty(INVALID_PAGE);
    }

    public String beerAlreadyExist() {
        return properties.getProperty(BEER_ALREADY_EXIST);
    }

    public String verifierNotFound() {
        return properties.getProperty(VERIFIER_NOT_FOUND);
    }

    public String wrongLoginOrPassword() {
        return properties.getProperty(WRONG_LOGIN_OR_PASSWORD);
    }

    public String loginOrEmailTaken() {
        return properties.getProperty(LOGIN_OR_EMAIL_TAKEN);
    }

    public String beerWithIdNotFound() {
        return properties.getProperty(BEER_WITH_ID_NOT_FOUND);
    }

    public String loginEmpty() {
        return properties.getProperty(LOGIN_EMPTY);
    }

    public String passwordEmpty() {
        return properties.getProperty(PASSWORD_EMPTY);
    }

    public String emailEmpty() {
        return properties.getProperty(EMAIL_EMPTY);
    }

    public String commandNotFound() {
        return properties.getProperty(COMMAND_NOT_FOUND);
    }

    public String nameEmpty() {
        return properties.getProperty(NAME_EMPTY);
    }

    public String containerEmpty() {
        return properties.getProperty(CONTAINER_EMPTY);
    }

    public String typeEmpty() {
        return properties.getProperty(TYPE_EMPTY);
    }

    public String abvEmpty() {
        return properties.getProperty(ABV_EMPTY);
    }

    public String ibuEmpty() {
        return properties.getProperty(IBU_EMPTY);
    }

    public String quantitySmall() {
        return properties.getProperty(QUANTITY_SMALL);
    }

    public String volumeSmall() {
        return properties.getProperty(VOLUME_SMALL);
    }

    public String bottleQuantitySmall() {
        return properties.getProperty(BOTTLE_QUANTITY_SMALL);
    }

    public String incorrectVolume() {
        return properties.getProperty(INCORRECT_VOLUME);
    }

    public String beerIdEmpty() {
        return properties.getProperty(BEER_ID_EMPTY);
    }

    public String volumeEmpty() {
        return properties.getProperty(VOLUME_EMPTY);
    }

    public String quantityEmpty() {
        return properties.getProperty(QUANTITY_EMPTY);
    }

    public String idNull() {
        return properties.getProperty(ID_NULL);
    }

    public String quantityNull() {
        return properties.getProperty(QUANTITY_NULL);
    }
}
