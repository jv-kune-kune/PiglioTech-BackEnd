package org.kunekune.PiglioTech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kunekune.PiglioTech.model.Book;
import org.kunekune.PiglioTech.repository.GoogleBooksDAO;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class HealthServiceTest {

    @Mock
    private DataSource mockDataSource;

    @Mock
    private GoogleBooksDAO mockGoogleBooksDAO;

    private HealthService healthService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        healthService = new HealthService(mockDataSource,mockGoogleBooksDAO);
    }

    @Test
    @DisplayName("checkHealth returns all systems healthy")
    void test_checkHealth_allHealthy() throws SQLException {

        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(2)).thenReturn(true);

        when(mockGoogleBooksDAO.fetchBookByIsbn(anyString()))
                .thenReturn(new Book("12345", "Test Title", "Test Author", "2023", "http://example.com/image", "Test Description"));

        Map<String, String> healthStatus = healthService.checkHealth();


        assertEquals("Healthy", healthStatus.get("application"));
        assertEquals("Healthy", healthStatus.get("database"));
        assertEquals("Healthy", healthStatus.get("googleBooksApi"));

        verify(mockDataSource).getConnection();
        verify(mockGoogleBooksDAO).fetchBookByIsbn(anyString());
    }

    @Test
    @DisplayName("checkHealth handles database connection failure")
    void test_checkHealth_databaseUnhealthy() throws SQLException {

        when(mockDataSource.getConnection()).thenThrow(new SQLException("Database connection failed"));

        Map<String, String> healthStatus = healthService.checkHealth();

        assertEquals("Unhealthy - Database connection failed", healthStatus.get("database"));
        assertEquals("Healthy", healthStatus.get("application"));
        assertNull(healthStatus.get("googleBooksApi"));

        verify(mockDataSource).getConnection();
        verifyNoInteractions(mockGoogleBooksDAO);
    }

    @Test
    @DisplayName("checkHealth handles Google Books API failure")
    void test_checkHealth_googleBooksApiUnhealthy() throws SQLException {
        // mock the database connection to return a valid connection
        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isValid(2)).thenReturn(true); // Mock database connection as healthy

        // mock the GoogleBooksDAO to throw an exception
        doThrow(new RuntimeException("API is down")).when(mockGoogleBooksDAO).fetchBookByIsbn(anyString());
        Map<String, String> healthStatus = healthService.checkHealth();


        assertEquals("Healthy", healthStatus.get("application"));
        assertEquals("Healthy", healthStatus.get("database"));
        assertEquals("Unhealthy - API is down", healthStatus.get("googleBooksApi"));

        verify(mockGoogleBooksDAO).fetchBookByIsbn(anyString());
        verify(mockDataSource).getConnection();
    }
}
