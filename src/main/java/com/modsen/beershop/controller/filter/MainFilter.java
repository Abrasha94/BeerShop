package com.modsen.beershop.controller.filter;

import com.modsen.beershop.service.ResponseService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "MainFilter")
public class MainFilter implements Filter {

    public static final String LOGIN = "/login";
    public static final String REGISTRATION = "/registration";
    public static final String SING_IN_MESSAGE = "Sing in";
    public static final String ROLE = "role";
    public static final String USER = "user";
    public static final String HISTORY = "/history";
    public static final String ADMIN = "admin";
    public static final String USERS_HISTORY = "/users-history";
    public static final String NOT_FOUND_MESSAGE = "Not Found";
    public static final String ITEMS = "/items";
    public static final String BEERS = "/beers";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;
        final HttpSession session = req.getSession(false);
        final String path = req.getPathInfo();

        if (path.startsWith(LOGIN) | path.startsWith(REGISTRATION)) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            if (session == null) {
                ResponseService.INSTANCE.send(res, SING_IN_MESSAGE, HttpServletResponse.SC_BAD_REQUEST);
            } else {
                if (session.getAttribute(ROLE).equals(USER)) {
                    if (path.startsWith(BEERS) | path.startsWith(ITEMS) | path.startsWith(HISTORY)) {
                        chain.doFilter(servletRequest, servletResponse);
                    }
                }
                if (session.getAttribute(ROLE).equals(ADMIN)) {
                    if (path.startsWith(BEERS) | path.startsWith(USERS_HISTORY)) {
                        chain.doFilter(servletRequest, servletResponse);
                    }
                }
                ResponseService.INSTANCE.send(res, NOT_FOUND_MESSAGE, HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    public void destroy() {
    }
}
