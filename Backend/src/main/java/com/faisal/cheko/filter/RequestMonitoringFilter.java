package com.faisal.cheko.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to monitor and log client requests.
 * Extends OncePerRequestFilter to ensure it's only executed once per request.
 */
@Component
public class RequestMonitoringFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestMonitoringFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        response.addHeader("X-Request-ID", requestId);

        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();

        log.info("Request started: {} {} {} from IP: {}, User-Agent: {}, Query: {}",
                requestId, requestMethod, requestUri, clientIp, userAgent, queryString);

        long startTime = System.currentTimeMillis();
        
        try {
            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Calculate request duration
            long duration = System.currentTimeMillis() - startTime;
            
            // Log request completion
            log.info("Request completed: {} {} {} - Status: {}, Duration: {}ms",
                    requestId, requestMethod, requestUri, response.getStatus(), duration);
            
            // Clean up MDC
            MDC.remove("requestId");
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        
        // If the IP contains multiple addresses (X-Forwarded-For can contain a comma-separated list),
        // take the first one
        if (clientIp != null && clientIp.contains(",")) {
            clientIp = clientIp.split(",")[0].trim();
        }
        
        return clientIp;
    }
}