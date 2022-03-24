package com.modsen.beershop.controller;

import com.modsen.beershop.config.Messages;
import com.modsen.beershop.controller.command.BuyBeerCommand;
import com.modsen.beershop.controller.command.Command;
import com.modsen.beershop.controller.command.CreateBeerCommand;
import com.modsen.beershop.controller.command.ListOfAvailableBeersCommand;
import com.modsen.beershop.controller.command.LoginCommand;
import com.modsen.beershop.controller.command.ReadAllUsersHistoryCommand;
import com.modsen.beershop.controller.command.ReadUserHistoryCommand;
import com.modsen.beershop.controller.command.RegistrationCommand;
import com.modsen.beershop.controller.command.UpdateBeerCommand;
import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.controller.request.UpdateBeerRequest;
import com.modsen.beershop.model.BottleBeerDescription;
import com.modsen.beershop.model.BottleBeerOrder;
import com.modsen.beershop.model.DraftBeerDescription;
import com.modsen.beershop.model.DraftBeerOrder;
import com.modsen.beershop.service.*;
import com.modsen.beershop.service.exception.CommandNotFoundException;
import com.modsen.beershop.service.exception.ConfigurationException;
import com.modsen.beershop.service.exception.UnableToExecuteQueryException;
import com.modsen.beershop.service.validator.*;
import com.modsen.beershop.service.validator.verifier.BottleBeerOrderVerifier;
import com.modsen.beershop.service.validator.verifier.BottleBeerVerifier;
import com.modsen.beershop.service.validator.verifier.DraftBeerOrderVerifier;
import com.modsen.beershop.service.validator.verifier.DraftBeerVerifier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MainServlet", value = "/MainServlet")
public class MainServlet extends HttpServlet {

    private List<Command> postCommands;
    private List<Command> putCommands;
    private List<Command> getCommands;

    @Override
    public void init() throws ServletException {
        super.init();

        postCommands = List.of(
                new LoginCommand(
                        new LoginService(List.of(
                                new FieldValidator<>(LoginRequest::getLogin, Messages.MESSAGE.loginEmpty()),
                                new FieldValidator<>(LoginRequest::getPassword, Messages.MESSAGE.passwordEmpty())))),
                new RegistrationCommand(
                        new RegistrationService(List.of(
                                new FieldValidator<>(RegistrationRequest::getLogin, Messages.MESSAGE.loginEmpty()),
                                new FieldValidator<>(RegistrationRequest::getPassword, Messages.MESSAGE.passwordEmpty()),
                                new FieldValidator<>(RegistrationRequest::getEmail, Messages.MESSAGE.emailEmpty()),
                                new LoginValidator(),
                                new EmailValidator()))),
                new CreateBeerCommand(
                        new CreateBeerService(List.of(
                                new FieldValidator<>(CreateBeerRequest::getName, Messages.MESSAGE.nameEmpty()),
                                new FieldValidator<>(CreateBeerRequest::getContainer, Messages.MESSAGE.containerEmpty()),
                                new FieldValidator<>(CreateBeerRequest::getType, Messages.MESSAGE.typeEmpty()),
                                new FieldValidator<>(CreateBeerRequest::getAbv, Messages.MESSAGE.abvEmpty()),
                                new FieldValidator<>(CreateBeerRequest::getIbu, Messages.MESSAGE.ibuEmpty()),
                                new ContainerValidator(),
                                new AbvValidator(),
                                new IbuValidator()),
                                List.of(
                                        new DraftBeerVerifier(List.of(
                                                new FieldValidator<>(DraftBeerDescription::getQuantity, Messages.MESSAGE.quantitySmall()))),
                                        new BottleBeerVerifier(List.of(
                                                new FieldValidator<>(BottleBeerDescription::getContainerVolume, Messages.MESSAGE.volumeSmall()),
                                                new FieldValidator<>(BottleBeerDescription::getQuantity, Messages.MESSAGE.bottleQuantitySmall()),
                                                new ContainerVolumeValidator<>(BottleBeerDescription::getContainerVolume, Messages.MESSAGE.incorrectVolume())))))),
                new BuyBeerCommand(
                        new BuyBeerService(List.of(
                                new BottleBeerOrderValidatorVerifier(List.of(
                                        new FieldValidator<>(BottleBeerOrder::getId, Messages.MESSAGE.idNull()),
                                        new FieldValidator<>(BottleBeerOrder::getQuantity, Messages.MESSAGE.quantityNull())
                                )),
                                new DraftBeerOrderValidatorVerifier(List.of(
                                        new FieldValidator<>(DraftBeerOrder::getId, Messages.MESSAGE.idNull()),
                                        new FieldValidator<>(DraftBeerOrder::getQuantity, Messages.MESSAGE.quantityNull())
                                ))), List.of(
                                new BottleBeerOrderVerifier(),
                                new DraftBeerOrderVerifier()))));
        putCommands = List.of(
                new UpdateBeerCommand(
                        new UpdateBeerService(List.of(
                                new FieldValidator<>(UpdateBeerRequest::getId, Messages.MESSAGE.beerIdEmpty()),
                                new FieldValidator<>(UpdateBeerRequest::getContainerVolume, Messages.MESSAGE.volumeEmpty()),
                                new FieldValidator<>(UpdateBeerRequest::getQuantity, Messages.MESSAGE.quantityEmpty()),
                                new ContainerVolumeValidator<>(UpdateBeerRequest::getContainerVolume, Messages.MESSAGE.incorrectVolume())))));
        getCommands = List.of(new ReadUserHistoryCommand(), new ReadAllUsersHistoryCommand(), new ListOfAvailableBeersCommand());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            final Command command = getCommands.stream()
                    .filter(c -> c.filter(request))
                    .findFirst()
                    .orElseThrow(() -> new CommandNotFoundException(Messages.MESSAGE.commandNotFound()));
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
                    .orElseThrow(() -> new CommandNotFoundException(Messages.MESSAGE.commandNotFound()));
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
                    .orElseThrow(() -> new CommandNotFoundException(Messages.MESSAGE.commandNotFound()));
            command.execute(request, response);
        } catch (CommandNotFoundException | UnableToExecuteQueryException | ConfigurationException e) {
            ResponseService.INSTANCE.send(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
