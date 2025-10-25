package com.faisal.cheko.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor to handle requests before and after they're processed by controllers.
 * Provides more granular control over the request handling process than filters.
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);
    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        log.debug("Handler: {}", handler.getClass().getSimpleName());
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");

        String contentType = request.getContentType();
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            if (contentType == null || !contentType.contains("application/json")) {
                log.warn("Request with method {} missing proper Content-Type header", request.getMethod());
            }
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            log.debug("ModelAndView: {}", modelAndView.getViewName());
        }

        response.setHeader("X-Processed-By", "Cheko-API");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime != null) {
            long processingTime = System.currentTimeMillis() - startTime;
            log.debug("Request processing time: {}ms", processingTime);
            
            response.setHeader("X-Processing-Time-Ms", String.valueOf(processingTime));
        }
        
        if (ex != null) {
            log.error("Exception occurred during request processing: {}", ex.getMessage(), ex);
        }
    }
}