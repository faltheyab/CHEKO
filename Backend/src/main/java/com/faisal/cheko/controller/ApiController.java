package com.faisal.cheko.controller;

import com.faisal.cheko.service.ApplicationService;
import com.faisal.cheko.service.DatabaseConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApplicationService applicationService;
    private final DatabaseConnectionService databaseConnectionService;

    @Autowired
    public ApiController(ApplicationService applicationService,  DatabaseConnectionService databaseConnectionService) {
        this.applicationService = applicationService;
        this.databaseConnectionService = databaseConnectionService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> status = applicationService.getApplicationStatus();
        status.put("message", "API is running");

        return ResponseEntity.ok(status);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Cheko API");
        info.put("version", "1.0.0");
        info.put("description", "REST API for Cheko application");

        return ResponseEntity.ok(info);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(applicationService.getApplicationMetrics());
    }

    @GetMapping("/db/health")
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {

        boolean isConnected = databaseConnectionService.pingDatabase();

        Map<String, Object> response = new HashMap<>();
        response.put("status", isConnected ? "UP" : "DOWN");
        response.put("database", "PostgreSQL");
        response.put("timestamp", System.currentTimeMillis());

        if (!isConnected) {
            response.put("message", "Database connection is down. Automatic retry is in progress.");
            return ResponseEntity.status(503).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/db/reconnect")
    public ResponseEntity<Map<String, Object>> forceConnectionCheck() {
        boolean isConnected = databaseConnectionService.checkConnection();

        Map<String, Object> response = new HashMap<>();
        response.put("status", isConnected ? "UP" : "DOWN");
        response.put("message", isConnected
                ? "Database connection is healthy"
                : "Database connection is down. Automatic retry is in progress.");
        response.put("timestamp", System.currentTimeMillis());

        if (!isConnected) {
            return ResponseEntity.status(503).body(response);
        }

        return ResponseEntity.ok(response);
    }
}

