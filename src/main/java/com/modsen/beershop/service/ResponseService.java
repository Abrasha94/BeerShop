package com.modsen.beershop.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@Data
@RequiredArgsConstructor
public class ResponseService {

    private static final String HEADER = "Content-type";
    private static final String CONTENT_TYPE = "application/json";

    private final ObjectMapper objectMapper;


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
