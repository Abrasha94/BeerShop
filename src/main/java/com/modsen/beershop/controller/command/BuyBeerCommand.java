package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.BuyBeerRequest;
import com.modsen.beershop.service.BuyBeerService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exception.BeerNotFoundException;
import com.modsen.beershop.service.exception.ValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class BuyBeerCommand implements Command {
    public static final String BUCKET = "/bucket";
    public static final String POST = "POST";
    private final ObjectMapper objectMapper;
    private final BuyBeerService buyBeerService;

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        final BuyBeerRequest buyBeerRequest = objectMapper.readValue(httpServletRequest.getInputStream(), BuyBeerRequest.class);
        try {
            buyBeerService.buy(buyBeerRequest, (UUID) httpServletRequest.getSession().getAttribute("UUID"));
            httpServletResponse.resetBuffer();
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.flushBuffer();
        } catch (ValidateException | BeerNotFoundException | IOException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String pathInfo = httpServletRequest.getPathInfo();
        return pathInfo.equals(BUCKET) & httpServletRequest.getMethod().equals(POST);
    }
}
