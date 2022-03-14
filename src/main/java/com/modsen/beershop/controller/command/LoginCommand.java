package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.LoginRequest;
import com.modsen.beershop.model.User;
import com.modsen.beershop.service.LoginService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exceprion.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginCommand implements Command {

    public static final String USER = "user";
    public static final String ROLE = "role";
    public static final String UUID = "uuid";
    public static final String WELCOME_BACK_MESSAGE = "Welcome back, ";
    public static final String ADMIN = "admin";
    public static final String HELLO_ADMIN_MESSAGE = "Hello Admin";
    public static final String LOGIN = "/login";

    private final ObjectMapper objectMapper;
    private final LoginService loginService;

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        final LoginRequest loginRequest = objectMapper.readValue(httpServletRequest.getInputStream(),
                LoginRequest.class);
        try {
            final User user = loginService.login(loginRequest);
            if (user.getRole().equals(USER)) {
                httpServletRequest.getSession().setAttribute(ROLE, USER);
                httpServletRequest.getSession().setAttribute(UUID, user.getUuid());
                ResponseService.INSTANCE.send(httpServletResponse, WELCOME_BACK_MESSAGE + user.getLogin(),
                        HttpServletResponse.SC_OK);
            }
            if (user.getRole().equals(ADMIN)) {
                httpServletRequest.getSession().setAttribute(ROLE, ADMIN);
                ResponseService.INSTANCE.send(httpServletResponse, HELLO_ADMIN_MESSAGE, HttpServletResponse.SC_OK);
            }
        } catch (UserNotFoundException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String path = httpServletRequest.getPathInfo();
        return path.startsWith(LOGIN);
    }

}
