package com.modsen.beershop.controller;

import com.modsen.beershop.controller.command.*;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.controller.request.UpdateBeerRequest;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.model.BottleBeerOrder;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.model.DraftBeerOrder;
import com.modsen.beershop.service.*;
import com.modsen.beershop.service.exceprion.CommandNotFoundException;
import com.modsen.beershop.service.exceprion.ConfigurationException;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;
import com.modsen.beershop.service.validator.*;
import com.modsen.beershop.service.validator.verifier.BottleBeerOrderVerifier;
import com.modsen.beershop.service.validator.verifier.BottleBeerVerifier;
import com.modsen.beershop.service.validator.verifier.DraftBeerOrderVerifier;
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
    public static final String ID_FIELD_IS_EMPTY = "Id field is empty!";
    public static final String CONTAINER_VOLUME_FIELD_IS_EMPTY = "Container volume field is empty!";
    public static final String QUANTITY_FIELD_IS_EMPTY = "Quantity field is empty!";
    public static final String ID_IS_NULL = "Id is null!";
    public static final String QUANTITY_IS_NULL = "Quantity is null!";

    private List<Command> postCommands;
    private List<Command> putCommands;
    private List<Command> getCommands;

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
                                new FieldValidator<>(CreateBeerRequest::getName, NAME_FIELD_IS_EMPTY),
                                new FieldValidator<>(CreateBeerRequest::getContainer, CONTAINER_FIELD_IS_EMPTY),
                                new FieldValidator<>(CreateBeerRequest::getType, TYPE_FIELD_IS_EMPTY),
                                new FieldValidator<>(CreateBeerRequest::getAbv, ABV_FIELD_IS_EMPTY),
                                new FieldValidator<>(CreateBeerRequest::getIbu, IBU_FIELD_IS_EMPTY),
                                new ContainerValidator(),
                                new AbvValidator(),
                                new IbuValidator()),
                                List.of(
                                        new DraftBeerVerifier(List.of(
                                                new FieldValidator<>(DraftBeerDescription::getQuantity, QUANTITY_OF_DRAFT_BEER_ERROR))),
                                        new BottleBeerVerifier(List.of(
                                                new FieldValidator<>(BottleBeerDescription::getContainerVolume, CONTAINER_VOLUME_IS_SMALL_ERROR),
                                                new FieldValidator<>(BottleBeerDescription::getQuantity, QUANTITY_OF_BOTTLE_IS_SMALL_ERROR),
                                                new ContainerVolumeValidator<>(BottleBeerDescription::getContainerVolume, INCORRECT_CONTAINER_VOLUME_ERROR)))))),
                new BuyBeerCommand(objectMapper,
                        new BuyBeerService(List.of(
                                new BottleBeerOrderValidatorVerifier(List.of(
                                        new FieldValidator<>(BottleBeerOrder::getId, ID_IS_NULL),
                                        new FieldValidator<>(BottleBeerOrder::getQuantity, QUANTITY_IS_NULL)
                                )),
                                new DraftBeerOrderValidatorVerifier(List.of(
                                        new FieldValidator<>(DraftBeerOrder::getId, ID_IS_NULL),
                                        new FieldValidator<>(DraftBeerOrder::getQuantity, QUANTITY_IS_NULL)
                                ))), List.of(
                                new BottleBeerOrderVerifier(),
                                new DraftBeerOrderVerifier()))));
        putCommands = List.of(
                new UpdateBeerCommand(objectMapper,
                        new UpdateBeerService(List.of(
                                new FieldValidator<>(UpdateBeerRequest::getId, ID_FIELD_IS_EMPTY),
                                new FieldValidator<>(UpdateBeerRequest::getContainerVolume, CONTAINER_VOLUME_FIELD_IS_EMPTY),
                                new FieldValidator<>(UpdateBeerRequest::getQuantity, QUANTITY_FIELD_IS_EMPTY),
                                new ContainerVolumeValidator<>(UpdateBeerRequest::getContainerVolume, INCORRECT_CONTAINER_VOLUME_ERROR)))));
        getCommands = List.of(new ReadUserHistoryCommand(), new ReadAllUsersHistoryCommand(), new ListOfAvailableBeersCommand());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            final Command command = getCommands.stream()
                    .filter(c -> c.filter(request))
                    .findFirst()
                    .orElseThrow(() -> new CommandNotFoundException(COMMAND_NOT_FOUND_MESSAGE));
            command.execute(request, response);
        } catch (CommandNotFoundException | UnableToExecuteQueryException | ConfigurationException | IOException e) {
            ResponseService.INSTANCE.send(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            final Command command = putCommands.stream()
                    .filter(c -> c.filter(request))
                    .findFirst()
                    .orElseThrow(() -> new CommandNotFoundException(COMMAND_NOT_FOUND_MESSAGE));
            command.execute(request, response);
        } catch (CommandNotFoundException | UnableToExecuteQueryException | ConfigurationException | IOException e) {
            ResponseService.INSTANCE.send(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
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
