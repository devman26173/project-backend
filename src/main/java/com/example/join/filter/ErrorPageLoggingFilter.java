package com.example.join.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ErrorPageLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        int status = response.getStatus();
        if (status == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String pathWithQuery = (query == null) ? uri : uri + "?" + query;

            log.error("[ERROR_PAGE_500] method={}, path={}, status={}", method, pathWithQuery, status);
        }
    }
}
