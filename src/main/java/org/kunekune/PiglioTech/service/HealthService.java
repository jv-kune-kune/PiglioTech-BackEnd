package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.repository.GoogleBooksDAO;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

    private final Datasource datasource;
    private final GoogleBooksDAO googleBooksDAO;

    public HealthService(DataSource dataSource, GoogleBooksDAO googleBooksDAO) {
        this.datasource = dataSource;
        this.googleBooksDAO = googleBooksDAO;
    }

    public Map<String, String> checkHealth() {
        Map<String, String> healthStatus = new HashMap<>();

        healthStatus.put("application", "Healthy");

        // check for Neon database
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                healthStatus.put("database", "Healthy");
            } else {
                healthStatus.put("database", "Unhealthy");
            }
        } catch (Exception e) {
            healthStatus.put("database", "Unhealthy - " + e.getMessage());
        }

        // check Google Books API
        try {
            googleBooksDAO.fetchBookByIsbn("9780134685991");       // dummy valid ISBN for testing
            healthStatus.put("googleBooksApi", "Healthy");
        } catch (Exception e) {
            healthStatus.put("googleBooksApi", "Unhealthy - " + e.getMessage());
        }

        return healthStatus;

    }
}

