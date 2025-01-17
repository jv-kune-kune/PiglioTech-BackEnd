package org.kunekune.PiglioTech.exception;

import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Common keys for response body
    private static final String RESPONSE_KEY_ERROR = "error";
    private static final String RESPONSE_KEY_MESSAGE = "message";

    // Specific response values
    private static final String ERROR_NOT_FOUND = "Not Found";
    private static final String ERROR_SERVICE = "Service Error";
    private static final String ERROR_VALIDATION = "Validation Error";
    private static final String ERROR_CONFLICT = "Item already exists";

    // Default messages
    private static final String MESSAGE_RESOURCE_NOT_FOUND = "The requested resource could not be found";
    private static final String MESSAGE_UNKNOWN_SERVICE_ERROR = "Unknown service error";
    private static final String MESSAGE_NO_API_RESPONSE = "No API response";


  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String, Object>> handleNoSuchElement(NoSuchElementException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERROR_NOT_FOUND, MESSAGE_RESOURCE_NOT_FOUND, RESPONSE_KEY_MESSAGE,
        e.getMessage() != null ? e.getMessage() : MESSAGE_RESOURCE_NOT_FOUND));
  }

  @ExceptionHandler(ApiServiceException.class)
  public ResponseEntity<Map<String, Object>> handleApiServiceError(ApiServiceException e) {
      HttpStatus status = e.getStatus() != null ? e.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
      String message = e.getMessage() != null ? e.getMessage() : MESSAGE_UNKNOWN_SERVICE_ERROR;
      String apiResponse = e.getApiResponse() != null ? e.getApiResponse() : MESSAGE_NO_API_RESPONSE;

      return ResponseEntity.status(status)
              .body(Map.of(
                      RESPONSE_KEY_ERROR, ERROR_SERVICE,
                      RESPONSE_KEY_MESSAGE, message,
                      MESSAGE_NO_API_RESPONSE, apiResponse
              ));
    }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of(RESPONSE_KEY_ERROR, ERROR_VALIDATION, RESPONSE_KEY_MESSAGE, e.getMessage()));
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<Map<String, Object>> handleEntityExistsException(EntityExistsException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of(RESPONSE_KEY_ERROR, ERROR_CONFLICT, RESPONSE_KEY_MESSAGE, e.getMessage()));
  }

}
