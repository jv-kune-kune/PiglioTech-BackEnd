package org.kunekune.PiglioTech.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.service.HealthService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.util.Map;

import static org.mockito.Mockito.when;

class HealthControllerTest {

    @Mock
    private HealthService mockHealthService;

    @Mock
    private DataSource mockDataSource;

    @InjectMocks
    private HealthController healthController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("GET / health returns 200 and health status map")
    void test_getHealthStatus() {
        Map<String, String> mockStatus = Map.of(
                "application", "Healthy",
                "database", "Healthy",
                "googleBooksApi", "Healthy" );

        when(mockHealthService.checkHealth()).thenReturn(mockStatus);

        ResponseEntity<Map<String, String>> response = healthController.getHealthStatus();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(mockStatus, response.getBody());
    }


}
