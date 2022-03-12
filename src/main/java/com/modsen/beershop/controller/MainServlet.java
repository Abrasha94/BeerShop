package com.modsen.beershop.controller;

import com.modsen.beershop.controller.command.Command;
import com.modsen.beershop.controller.command.LoginCommand;
import com.modsen.beershop.controller.command.RegistrationCommand;
import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.repository.UserRepository;
import com.modsen.beershop.service.LoginService;
import com.modsen.beershop.service.RegistrationService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.ValidateService;
import com.modsen.beershop.service.exceprion.CommandNotFoundException;
import com.modsen.beershop.service.exceprion.ConfigurationException;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;
import com.modsen.beershop.service.validator.EmailValidator;
import com.modsen.beershop.service.validator.FieldValidator;
import com.modsen.beershop.service.validator.LoginValidator;
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

    private List<Command> postCommands;
    private ResponseService responseService;

    @Override
    public void init() throws ServletException {
        super.init();
        final ObjectMapper objectMapper = new ObjectMapper();
        final ValidateService validateService = new ValidateService();
        final UserRepository userRepository = new UserRepository();
        responseService = new ResponseService(objectMapper);

        postCommands = List.of(
                new LoginCommand(objectMapper, responseService,
                        new LoginService(validateService, userRepository, List.of(
                                new FieldValidator<>(LoginRequest::getLogin, LOGIN_IS_EMPTY),
                                new FieldValidator<>(LoginRequest::getPassword, PASSWORD_IS_EMPTY)))),
                new RegistrationCommand(objectMapper, responseService,
                        new RegistrationService(validateService, userRepository, List.of(
                                new FieldValidator<>(RegistrationRequest::getLogin, LOGIN_IS_EMPTY),
                                new FieldValidator<>(RegistrationRequest::getPassword, PASSWORD_IS_EMPTY),
                                new FieldValidator<>(RegistrationRequest::getEmail, EMAIL_IS_EMPTY),
                                new LoginValidator(),
                                new EmailValidator()))));
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
            responseService.send(response, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
