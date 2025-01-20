package org.kunekune.PiglioTech.service;

import org.kunekune.PiglioTech.repository.GoogleBooksDAO;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthService {

  private final DataSource dataSource;
  private final GoogleBooksDAO googleBooksDAO;

  public HealthService(DataSource dataSource, GoogleBooksDAO googleBooksDAO) {
    this.dataSource = dataSource;
    this.googleBooksDAO = googleBooksDAO;
  }

  public Map<String, String> checkHealth() {
    Map<String, String> healthStatus = new HashMap<>();
    healthStatus.put("application", "Healthy");

    // check Neon database health
    try (Connection connection = dataSource.getConnection()) {
      if (connection.isValid(2)) {
        healthStatus.put("database", "Healthy");
      } else {
        healthStatus.put("database", "Unhealthy - Invalid connection");
        return healthStatus; // exit if DB is unhealthy
      }
    } catch (Exception e) {
      healthStatus.put("database", "Unhealthy - " + e.getMessage());
      return healthStatus; // exit if DB connection fails
    }

    // check Google Books API health
    try {
      googleBooksDAO.fetchBookByIsbn("9780134685991"); // dummy ISBN for testing
      healthStatus.put("googleBooksApi", "Healthy");
    } catch (Exception e) {
      healthStatus.put("googleBooksApi", "Unhealthy - " + e.getMessage());
    }

    return healthStatus;

  }
}

