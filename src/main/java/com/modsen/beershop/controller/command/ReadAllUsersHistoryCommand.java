package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.response.AllUsersHistoryResponse;
import com.modsen.beershop.service.ReadAllUsersHistoryService;
import com.modsen.beershop.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ReadAllUsersHistoryCommand implements Command {

    public static final String SIZE = "size";
    public static final String PAGE = "page";

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        try {
            final Integer size = Integer.valueOf(httpServletRequest.getParameter(SIZE));
            final Integer page = Integer.valueOf(httpServletRequest.getParameter(PAGE));
            final AllUsersHistoryResponse allUsersHistoryResponse = ReadAllUsersHistoryService.INSTANCE.read(page, size);
            ResponseService.INSTANCE.send(httpServletResponse, allUsersHistoryResponse, HttpServletResponse.SC_OK);
        } catch (NumberFormatException | IOException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String pathInfo = httpServletRequest.getPathInfo();
        return pathInfo.equals("/history/users") & httpServletRequest.getMethod().equals("GET");
    }
}
