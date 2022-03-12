package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.RegistrationRequest;
import com.modsen.beershop.controller.response.UserRegistryResponse;
import com.modsen.beershop.service.RegistrationService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exceprion.UserExistException;
import com.modsen.beershop.service.exceprion.ValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class RegistrationCommand implements Command {

    public static final String REGISTRATION = "/registration";
    private final ObjectMapper objectMapper;
    private final ResponseService responseService;
    private final RegistrationService registrationService;

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        final RegistrationRequest registrationRequest =
                objectMapper.readValue(httpServletRequest.getInputStream(), RegistrationRequest.class);
        try {
            final String uuid = registrationService.register(registrationRequest);
            responseService.send(httpServletResponse, new UserRegistryResponse(uuid), HttpServletResponse.SC_OK);
        } catch (UserExistException | ValidateException e) {
            responseService.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String path = httpServletRequest.getPathInfo();
        return path.startsWith(REGISTRATION);
    }
}
