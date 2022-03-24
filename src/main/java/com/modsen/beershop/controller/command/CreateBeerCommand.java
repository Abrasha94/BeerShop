package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.controller.response.AddBeerResponse;
import com.modsen.beershop.service.CreateBeerService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exception.BeerExistException;
import com.modsen.beershop.service.exception.BeerVerifierNotFoundException;
import com.modsen.beershop.service.exception.ValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static com.modsen.beershop.utils.ObjectMapperBean.objectMapper;

@RequiredArgsConstructor
public class CreateBeerCommand implements Command {

    public static final String BEERS = "/beers";
    public static final String POST = "POST";
    private final CreateBeerService createBeerService;

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        final CreateBeerRequest createBeerRequest = objectMapper.readValue(httpServletRequest.getInputStream(),
                CreateBeerRequest.class);
        try {
            final AddBeerResponse addBeerResponse = createBeerService.create(createBeerRequest);
            ResponseService.INSTANCE.send(httpServletResponse, addBeerResponse, HttpServletResponse.SC_OK);
        } catch (BeerExistException | BeerVerifierNotFoundException | ValidateException | IOException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String path = httpServletRequest.getPathInfo();
        return path.startsWith(BEERS) & httpServletRequest.getMethod().equals(POST);
    }
}
