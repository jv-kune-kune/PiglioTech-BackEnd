package org.kunekune.PiglioTech.exception;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();


    @Nested
    @DisplayName("Tests for handleNoSuchElement")
    class HandleNoSuchElementTests {

        @Test
        @DisplayName("Should return 404 NOT_FOUND and given message for NoSuchElementException with non-null message")
        void handleNoSuchElement_withMessage() {
            // Arrange
            NoSuchElementException exception = new NoSuchElementException("Custom not found message");

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleNoSuchElement(exception);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Not Found", response.getBody().get("error"));
            assertEquals("Custom not found message", response.getBody().get("message"));
        }

        @Test
        @DisplayName("Should return 404 NOT_FOUND and default message when NoSuchElementException has null message")
        void handleNoSuchElement_withNullMessage() {
            // Arrange
            NoSuchElementException exception = new NoSuchElementException((Throwable) null);

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleNoSuchElement(exception);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Not Found", response.getBody().get("error"));
            assertEquals("The requested resource could not be found", response.getBody().get("message"));
        }
    }

    @Nested
    @DisplayName("Tests for handleApiServiceError")
    class HandleApiServiceErrorTests {

        @Test
        @DisplayName("Returns the specified HTTP status and message when ApiServiceException has a non-null status")
        void handleApiServiceError_withStatus() {
            // Arrange
            // This constructor sets the status to HttpStatus.BAD_REQUEST
            ApiServiceException exception = new ApiServiceException(
                    "Some API response",
                    "Bad Request error",
                    HttpStatus.BAD_REQUEST
            );

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleApiServiceError(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Service Error", Objects.requireNonNull(response.getBody()).get("error"));
            assertEquals("Bad Request error", response.getBody().get("message"));
            assertEquals("Some API response", response.getBody().get("apiResponse"));
        }

        @Test
        @DisplayName("Defaults to 500 and uses a placeholder if apiResponse is null")
        void handleApiServiceError_withNullApiResponse() {
            // Arrange
            ApiServiceException exception = new ApiServiceException(
                    null,
                    "Null response test",
                    null
            );

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleApiServiceError(exception);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Service Error", Objects.requireNonNull(response.getBody()).get("error"));
            assertEquals("Null response test", response.getBody().get("message"));
            assertEquals("No API response", response.getBody().get("apiResponse"));
        }

        @Test
        @DisplayName("Uses INTERNAL_SERVER_ERROR when only apiResponse and message are provided")
        void handleApiServiceError_defaultStatusConstructor() {
            // Arrange
            // This constructor sets the status to INTERNAL_SERVER_ERROR by default
            ApiServiceException exception = new ApiServiceException(
                    "Minimal API response",
                    "Some default error message"
            );

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleApiServiceError(exception);

            // Assert
            // Because we used the (String apiResponse, String message) constructor,
            // we expect the default status: INTERNAL_SERVER_ERROR
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Service Error", Objects.requireNonNull(response.getBody()).get("error"));
            assertEquals("Some default error message", response.getBody().get("message"));
            assertEquals("Minimal API response", response.getBody().get("apiResponse"));
        }

        @Test
        @DisplayName("If apiResponse is null, returns 'No API response' string")
        void handleApiServiceError_messageAndStatusOnly() {
            // Arrange
            ApiServiceException exception = new ApiServiceException(
                    "Partial error",    // message
                    HttpStatus.SERVICE_UNAVAILABLE
            );

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleApiServiceError(exception);

            // Assert
            assertEquals("No API response", Objects.requireNonNull(response.getBody()).get("apiResponse"));
        }
    }


    @Nested
    @DisplayName("Tests for handleIllegalArgumentException")
    class HandleIllegalArgumentExceptionTests {

        @Test
        @DisplayName("Should return BAD_REQUEST and the given message for IllegalArgumentException")
        void handleIllegalArgumentException_withMessage() {
            // Arrange
            IllegalArgumentException exception = new IllegalArgumentException("Invalid argument message");

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgumentException(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Validation Error", response.getBody().get("error"));
            assertEquals("Invalid argument message", response.getBody().get("message"));
        }
    }

    @Nested
    @DisplayName("Tests for handleEntityExistsException")
    class HandleEntityExistsExceptionTests {

        @Test
        @DisplayName("Should return CONFLICT and the given message when entity already exists")
        void handleEntityExistsException() {
            // Arrange
            EntityExistsException exception = new EntityExistsException("Entity already present");

            // Act
            ResponseEntity<Map<String, Object>> response = handler.handleEntityExistsException(exception);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Item already exists", response.getBody().get("error"));
            assertEquals("Entity already present", response.getBody().get("message"));
        }
    }
}
