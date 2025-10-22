package com.faisal.cheko.controller;

import com.faisal.cheko.service.ApplicationService;
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

    @Autowired
    public ApiController(ApplicationService applicationService) {
        this.applicationService = applicationService;
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
}

