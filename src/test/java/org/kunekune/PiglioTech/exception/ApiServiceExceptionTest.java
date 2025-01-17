package org.kunekune.PiglioTech.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit tests for ApiServiceException")
class ApiServiceExceptionTest {

    @Nested
    @DisplayName("Tests for (String apiResponse, String message) constructor")
    class ConstructorApiResponseMessage {

        @Test
        @DisplayName("Should assign apiResponse, message, and default status INTERNAL_SERVER_ERROR")
        void shouldAssignFieldsCorrectly() {
            // Arrange
            String apiResponse = "API data";
            String message = "Some error occurred";

            // Act
            ApiServiceException exception = new ApiServiceException(apiResponse, message);

            // Assert
            assertEquals(apiResponse, exception.getApiResponse());
            assertEquals(message, exception.getMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        }
    }

    @Nested
    @DisplayName("Tests for (String apiResponse, String message, HttpStatus status) constructor")
    class ConstructorApiResponseMessageStatus {

        @Test
        @DisplayName("Should assign apiResponse, message, and custom status when provided")
        void shouldAssignAllFields() {
            // Arrange
            String apiResponse = "Some API response details";
            String message = "Custom message";
            HttpStatus customStatus = HttpStatus.BAD_REQUEST;

            // Act
            ApiServiceException exception = new ApiServiceException(apiResponse, message, customStatus);

            // Assert
            assertEquals(apiResponse, exception.getApiResponse());
            assertEquals(message, exception.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }

        @Test
        @DisplayName("Should handle a null HttpStatus gracefully (if that scenario is possible in your usage)")
        void shouldHandleNullStatus() {
            // Arrange
            String apiResponse = "Null status API response";
            String message = "Null status scenario";
            HttpStatus nullStatus = null;

            // Act
            ApiServiceException exception = new ApiServiceException(apiResponse, message, nullStatus);

            // Assert
            // This might be null, or you might consider defaulting to INTERNAL_SERVER_ERROR.
            // Adjust this assertion depending on your desired behavior.
            assertNull(exception.getStatus());
        }
    }

    @Nested
    @DisplayName("Tests for (String message, HttpStatus status) constructor")
    class ConstructorMessageStatus {

        @Test
        @DisplayName("Should assign message and custom status, with apiResponse as null")
        void shouldAssignMessageAndStatus() {
            // Arrange
            String message = "Partial constructor message";
            HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

            // Act
            ApiServiceException exception = new ApiServiceException(message, status);

            // Assert
            assertNull(exception.getApiResponse());
            assertEquals(message, exception.getMessage());
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatus());
        }
    }

    @Nested
    @DisplayName("Tests for the getters (getApiResponse, getStatus)")
    class GettersOnly {

        @Test
        @DisplayName("getApiResponse returns the apiResponse previously set")
        void getApiResponse() {
            // Arrange
            String expectedApiResponse = "Testing getApiResponse";
            String message = "Some message";
            ApiServiceException exception = new ApiServiceException(expectedApiResponse, message);

            // Act
            String actualApiResponse = exception.getApiResponse();

            // Assert
            assertEquals(expectedApiResponse, actualApiResponse);
        }

        @Test
        @DisplayName("getStatus returns the status previously set")
        void getStatus() {
            // Arrange
            HttpStatus expectedStatus = HttpStatus.CONFLICT;
            String apiResponse = "Sample response";
            String message = "Conflict occurred";
            ApiServiceException exception = new ApiServiceException(apiResponse, message, expectedStatus);

            // Act
            HttpStatus actualStatus = exception.getStatus();

            // Assert
            assertEquals(expectedStatus, actualStatus);
        }
    }
}
