package com.architecturepro.infrastructure.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestTimeZoneFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            RequestTimeZoneContext.setCurrentZoneId(request.getHeader(RequestTimeZoneContext.HEADER_NAME));
            filterChain.doFilter(request, response);
        } finally {
            RequestTimeZoneContext.clear();
        }
    }
}
