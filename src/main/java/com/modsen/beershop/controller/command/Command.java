package com.modsen.beershop.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface Command {
    void execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;

    boolean filter(HttpServletRequest httpServletRequest);
}
