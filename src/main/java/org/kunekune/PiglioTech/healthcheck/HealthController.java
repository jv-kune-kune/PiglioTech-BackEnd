package org.kunekune.PiglioTech.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private HealthService healthService;

    @GetMapping ("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> healthStatus = healthService.performHealthChecks();
        return ResponseEntity.ok(healthStatus);
    }
}
