package com.modsen.beershop.service;

import jakarta.servlet.http.HttpServletResponse;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static com.modsen.beershop.utils.ObjectMapperBean.objectMapper;

public enum ResponseService {
    INSTANCE;

    private static final String HEADER = "Content-type";



    public void create(HttpServletResponse httpServletResponse, Object dto, Integer code) throws IOException {
        final String jsonValue = objectMapper.writeValueAsString(dto);
        httpServletResponse.resetBuffer();
        httpServletResponse.setStatus(code);
        httpServletResponse.setHeader(HEADER, MediaType.APPLICATION_JSON);
        httpServletResponse.getOutputStream().print(jsonValue);
        httpServletResponse.flushBuffer();
    }

    public void send(HttpServletResponse httpServletResponse, Object dto, Integer code) throws IOException {
        create(httpServletResponse, dto, code);
    }
}
