package com.modsen.beershop.service;

import jakarta.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public enum ResponseService {
    INSTANCE;

    private static final String HEADER = "Content-type";
    private static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper objectMapper = new ObjectMapper();


    public void create(HttpServletResponse httpServletResponse, Object dto, Integer code) throws IOException {
        final String jsonValue = objectMapper.writeValueAsString(dto);
        httpServletResponse.resetBuffer();
        httpServletResponse.setStatus(code);
        httpServletResponse.setHeader(HEADER, CONTENT_TYPE);
        httpServletResponse.getOutputStream().print(jsonValue);
        httpServletResponse.flushBuffer();
    }

    public void send(HttpServletResponse httpServletResponse, Object dto, Integer code) throws IOException {
        create(httpServletResponse, dto, code);
    }
}
