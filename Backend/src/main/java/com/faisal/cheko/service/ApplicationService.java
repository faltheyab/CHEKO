package com.faisal.cheko.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    public Map<String, Object> getApplicationStatus() {
        log.info("Retrieving application status");
        
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("database", "PostgreSQL");
        status.put("version", "1.0.0");
        status.put("timestamp", System.currentTimeMillis());
        
        return status;
    }

    public Map<String, Object> getApplicationMetrics() {
        log.info("Retrieving application metrics");
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("memory", Runtime.getRuntime().totalMemory() / (1024 * 1024) + "MB");
        metrics.put("processors", Runtime.getRuntime().availableProcessors());
        metrics.put("uptime", System.currentTimeMillis());
        
        return metrics;
    }
}