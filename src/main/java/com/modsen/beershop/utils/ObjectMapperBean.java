package com.modsen.beershop.utils;

import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperBean {

    private ObjectMapperBean() {
    }

    public static final ObjectMapper objectMapper = new ObjectMapper();
}
