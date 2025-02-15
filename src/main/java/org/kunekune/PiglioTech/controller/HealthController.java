package org.kunekune.PiglioTech.controller;

import org.kunekune.PiglioTech.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private final HealthService healthService;

    @Autowired
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping ("/health")
    public ResponseEntity<Map<String, String>> getHealthStatus() {
        Map<String, String> healthStatus = healthService.checkHealth();
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }
}
