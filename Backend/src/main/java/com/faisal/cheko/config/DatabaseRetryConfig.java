package com.faisal.cheko.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for database connection retry mechanism.
 * Uses Spring Retry to automatically retry database connections when they fail.
 */
@Configuration
@EnableRetry
public class DatabaseRetryConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseRetryConfig.class);
    
    // Retry every 5 minutes
    private static final long RETRY_BACKOFF_PERIOD_MS = 300000;
    
    // Maximum number of retries 288 retries * 5min = 1440 min = 24 hours
    private static final int MAX_RETRIES = 288;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(RETRY_BACKOFF_PERIOD_MS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(org.springframework.dao.DataAccessException.class, true);
        retryableExceptions.put(org.springframework.dao.TransientDataAccessException.class, true);
        retryableExceptions.put(org.springframework.dao.QueryTimeoutException.class, true);
        retryableExceptions.put(org.springframework.dao.RecoverableDataAccessException.class, true);
        retryableExceptions.put(java.sql.SQLException.class, true);
        retryableExceptions.put(java.net.ConnectException.class, true);
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(MAX_RETRIES, retryableExceptions, true);
        retryTemplate.setRetryPolicy(retryPolicy);
        
        return retryTemplate;
    }
}
