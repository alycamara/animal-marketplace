package com.camara.animalmarketplace.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString != null ? uri + "?" + queryString : uri;

        if (request.getMethod().equalsIgnoreCase("GET")
                && !uri.equals("/login")
                && !uri.equals("/favicon.ico")
                && !uri.startsWith("/css")
                && !uri.startsWith("/js")
                && !uri.startsWith("/images")) {
            logger.info("Captured URL for redirection: {}", fullUrl);
            request.getSession().setAttribute("redirectUrl", fullUrl);
        }
        filterChain.doFilter(request, response);
    }
}