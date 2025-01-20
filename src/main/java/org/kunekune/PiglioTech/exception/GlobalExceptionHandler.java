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

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String, Object>> handleNoSuchElement(NoSuchElementException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Not Found", "message",
        e.getMessage() != null ? e.getMessage() : "The requested resource could not be found"));
  }

  @ExceptionHandler(ApiServiceException.class)
  public ResponseEntity<Map<String, Object>> handleApiServiceError(ApiServiceException e) {
    return ResponseEntity
        .status(e.getStatus() != null ? e.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "Service Error", "message", e.getMessage(), "apiResponse",
            e.getApiResponse()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "Validation Error", "message", e.getMessage()));
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<String> handleEntityConflict(EntityExistsException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }
}
