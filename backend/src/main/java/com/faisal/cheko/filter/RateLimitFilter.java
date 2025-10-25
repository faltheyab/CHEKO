package com.faisal.cheko.filter;

import com.faisal.cheko.service.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to enforce rate limits on API requests.
 * Uses the RateLimiterService to track and limit requests from each client.
 * Extends OncePerRequestFilter to ensure it's only executed once per request.
 */
@Component
@Order(1) // Execute this filter first
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int DEFAULT_RETRY_AFTER_SECONDS = 60;

    private final RateLimiterService rateLimiterService;

    @Autowired
    public RateLimitFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        if (!path.startsWith("/api/") || path.equals("/api/health")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String clientIp = getClientIp(request);
        int requestCost = getRequestCost(request);
        boolean allowed = rateLimiterService.allowRequest(clientIp, requestCost);
        
        if (!allowed) {
            int remainingTokens = rateLimiterService.getRemainingTokens(clientIp);
            
            response.setHeader("X-RateLimit-Limit", "20");
            response.setHeader("X-RateLimit-Remaining", String.valueOf(remainingTokens));
            response.setHeader("X-RateLimit-Reset", String.valueOf(DEFAULT_RETRY_AFTER_SECONDS));
            response.setHeader("Retry-After", String.valueOf(DEFAULT_RETRY_AFTER_SECONDS));
            
            // Set response status and write error message
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                    "{\"status\":%d,\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Try again in %d seconds.\"}",
                    HttpStatus.TOO_MANY_REQUESTS.value(), DEFAULT_RETRY_AFTER_SECONDS));
            
            log.warn("Rate limit exceeded for client IP: {}", clientIp);
            return;
        }
        
        int remainingTokens = rateLimiterService.getRemainingTokens(clientIp);
        response.setHeader("X-RateLimit-Limit", "20");
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remainingTokens));
        
        filterChain.doFilter(request, response);
    }

    private int getRequestCost(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        
        // Assign higher costs to write operations
        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
            return 2;
        }
        
        // Assign higher costs to expensive endpoints
        if (path.contains("/api/users") && "GET".equals(method)) {
            return 2;
        }
        
        // Default cost for other requests
        return 1;
    }

    /**
     * Extract the client IP address from the request.
     * Handles cases where the request comes through a proxy or load balancer.
     *
     * @param request the HTTP request
     * @return the client IP address
     */
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