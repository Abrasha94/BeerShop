package com.modsen.beershop.controller.command;

import com.modsen.beershop.controller.response.UserHistoryResponse;
import com.modsen.beershop.service.ReadUserHistoryService;
import com.modsen.beershop.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

public class ReadUserHistoryCommand implements Command {

    public static final String USER_UUID = "UUID";
    public static final String SIZE = "size";
    public static final String PAGE = "page";

    @Override
    public void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        try {
            final UUID uuid = (UUID) httpServletRequest.getSession().getAttribute(USER_UUID);
            final Integer size = Integer.valueOf(httpServletRequest.getParameter(SIZE));
            final Integer page = Integer.valueOf(httpServletRequest.getParameter(PAGE));
            final UserHistoryResponse userHistoryResponse = ReadUserHistoryService.INSTANCE.read(page, size, uuid);
            ResponseService.INSTANCE.send(httpServletResponse, userHistoryResponse, HttpServletResponse.SC_OK);
        } catch (NumberFormatException | IOException e) {
            ResponseService.INSTANCE.send(httpServletResponse, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public boolean filter(HttpServletRequest httpServletRequest) {
        final String pathInfo = httpServletRequest.getPathInfo();
        return pathInfo.equals("/history/user") & httpServletRequest.getMethod().equals("GET");
    }
}
