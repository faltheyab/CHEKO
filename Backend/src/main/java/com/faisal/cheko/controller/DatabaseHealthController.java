package com.faisal.cheko.controller;

import com.faisal.cheko.service.DatabaseConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/db")
public class DatabaseHealthController {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthController.class);
    
    private final DatabaseConnectionService databaseConnectionService;

    @Autowired
    public DatabaseHealthController(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkDatabaseHealth() {
        log.info("Checking database health");
        
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

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> forceConnectionCheck() {
        log.info("Forcing database connection check");
        
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
