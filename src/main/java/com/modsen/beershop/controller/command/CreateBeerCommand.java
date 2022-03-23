package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.request.CreateBeerRequest;
import com.modsen.beershop.controller.response.AddBeerResponse;
import com.modsen.beershop.service.CreateBeerService;
import com.modsen.beershop.service.ResponseService;
import com.modsen.beershop.service.exceprion.BeerExistException;
import com.modsen.beershop.service.exceprion.BeerVerifierNotFoundException;
import com.modsen.beershop.service.exceprion.ValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class CreateBeerCommand implements Command {

    public static final String BEERS = "/beers";
    public static final String POST = "POST";
    private final ObjectMapper objectMapper;
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
