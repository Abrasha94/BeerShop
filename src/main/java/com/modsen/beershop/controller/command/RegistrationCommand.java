package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.controller.response.RegistrationResponse;
import com.modsen.beershop.service.RegistrationService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exception.UserExistException;
import com.modsen.beershop.service.exception.ValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

import static com.modsen.beershop.utils.ObjectMapperBean.objectMapper;

@RequiredArgsConstructor
public class RegistrationCommand implements Command {

    public static final String REGISTRATION = "/registration";
    private final RegistrationService registrationService;

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        final RegistrationRequest registrationRequest =
                objectMapper.readValue(httpServletRequest.getInputStream(), RegistrationRequest.class);
        try {
            final UUID uuid = registrationService.register(registrationRequest);
            ResponseService.INSTANCE.send(httpServletResponse, new RegistrationResponse(uuid), HttpServletResponse.SC_OK);
        } catch (UserExistException | ValidateException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String path = httpServletRequest.getPathInfo();
        return path.startsWith(REGISTRATION);
    }
}
