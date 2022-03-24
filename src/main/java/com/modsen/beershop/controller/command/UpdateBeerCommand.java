package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.UpdateBeerRequest;
import com.modsen.beershop.controller.response.UpdateBeerResponse;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.UpdateBeerService;
import com.modsen.beershop.service.exception.BeerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class UpdateBeerCommand implements Command {
    private final ObjectMapper objectMapper;
    private final UpdateBeerService updateBeerService;

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        final UpdateBeerRequest updateBeerRequest =
                objectMapper.readValue(httpServletRequest.getInputStream(), UpdateBeerRequest.class);
        try {
            final UpdateBeerResponse updateBeerResponse = updateBeerService.update(updateBeerRequest);
            ResponseService.INSTANCE.send(httpServletResponse, updateBeerResponse, HttpServletResponse.SC_OK);
        } catch (BeerNotFoundException | IOException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String pathInfo = httpServletRequest.getPathInfo();
        return pathInfo.equals("/beers") & httpServletRequest.getMethod().equals("PUT");
    }
}
