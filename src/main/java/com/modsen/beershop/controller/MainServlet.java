package com.modsen.beershop.controller;

import com.modsen.beershop.controller.command.CreateBeerCommand;
import com.modsen.beershop.controller.request.AddBeerRequest;
import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.controller.command.Command;
import com.modsen.beershop.controller.command.LoginCommand;
import com.modsen.beershop.controller.command.RegistrationCommand;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.service.CreateBeerService;
import com.modsen.beershop.service.LoginService;
import com.modsen.beershop.service.RegistrationService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exceprion.CommandNotFoundException;
import com.modsen.beershop.service.exceprion.ConfigurationException;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;
import com.modsen.beershop.service.validator.*;
import com.modsen.beershop.service.validator.verifier.BottleBeerVerifier;
import com.modsen.beershop.service.validator.verifier.DraftBeerVerifier;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MainServlet", value = "/MainServlet")
public class MainServlet extends HttpServlet {

    public static final String LOGIN_IS_EMPTY = "Login is empty!";
    public static final String PASSWORD_IS_EMPTY = "Password is empty!";
    public static final String EMAIL_IS_EMPTY = "Email is empty!";
    public static final String COMMAND_NOT_FOUND_MESSAGE = "Command not found!";
    public static final String NAME_FIELD_IS_EMPTY = "Name field is empty!";
    public static final String CONTAINER_FIELD_IS_EMPTY = "Container field is empty!";
    public static final String TYPE_FIELD_IS_EMPTY = "Type field is empty!";
    public static final String ABV_FIELD_IS_EMPTY = "ABV field is empty!";
    public static final String IBU_FIELD_IS_EMPTY = "IBU field is empty!";
    public static final String QUANTITY_OF_DRAFT_BEER_ERROR = "Quantity of draft beer is too small!";
    public static final String CONTAINER_VOLUME_IS_SMALL_ERROR = "Container volume is too small!";
    public static final String QUANTITY_OF_BOTTLE_IS_SMALL_ERROR = "Quantity of bottle is too small!";
    public static final String INCORRECT_CONTAINER_VOLUME_ERROR = "Incorrect container volume!";

    private List<Command> postCommands;

    @Override
    public void init() throws ServletException {
        super.init();
        final ObjectMapper objectMapper = new ObjectMapper();

        postCommands = List.of(
                new LoginCommand(objectMapper,
                        new LoginService(List.of(
                                new FieldValidator<>(LoginRequest::getLogin, LOGIN_IS_EMPTY),
                                new FieldValidator<>(LoginRequest::getPassword, PASSWORD_IS_EMPTY)))),
                new RegistrationCommand(objectMapper,
                        new RegistrationService(List.of(
                                new FieldValidator<>(RegistrationRequest::getLogin, LOGIN_IS_EMPTY),
                                new FieldValidator<>(RegistrationRequest::getPassword, PASSWORD_IS_EMPTY),
                                new FieldValidator<>(RegistrationRequest::getEmail, EMAIL_IS_EMPTY),
                                new LoginValidator(),
                                new EmailValidator()))),
                new CreateBeerCommand(objectMapper,
                        new CreateBeerService(List.of(
                                new FieldValidator<>(AddBeerRequest::getName, NAME_FIELD_IS_EMPTY),
                                new FieldValidator<>(AddBeerRequest::getContainer, CONTAINER_FIELD_IS_EMPTY),
                                new FieldValidator<>(AddBeerRequest::getType, TYPE_FIELD_IS_EMPTY),
                                new FieldValidator<>(AddBeerRequest::getAbv, ABV_FIELD_IS_EMPTY),
                                new FieldValidator<>(AddBeerRequest::getIbu, IBU_FIELD_IS_EMPTY),
                                new ContainerValidator(),
                                new AbvValidator(),
                                new IbuValidator()
                        ), List.of(
                                new DraftBeerVerifier(List.of(
                                        new FieldValidator<>(DraftBeerDescription::getQuantity, QUANTITY_OF_DRAFT_BEER_ERROR)
                                )),
                                new BottleBeerVerifier(List.of(
                                        new FieldValidator<>(BottleBeerDescription::getContainerVolume, CONTAINER_VOLUME_IS_SMALL_ERROR),
                                        new FieldValidator<>(BottleBeerDescription::getQuantity, QUANTITY_OF_BOTTLE_IS_SMALL_ERROR),
                                        new ContainerVolumeValidator<>(BottleBeerDescription::getContainerVolume, INCORRECT_CONTAINER_VOLUME_ERROR)
                                ))))));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            final Command command = postCommands.stream()
                    .filter(c -> c.filter(request))
                    .findFirst()
                    .orElseThrow(() -> new CommandNotFoundException(COMMAND_NOT_FOUND_MESSAGE));
            command.execute(request, response);
        } catch (CommandNotFoundException | UnableToExecuteQueryException | ConfigurationException e) {
            ResponseService.INSTANCE.send(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
