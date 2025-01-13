package org.kunekune.PiglioTech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiServiceException.class)
    public ResponseEntity<String> handleApiServiceError(ApiServiceException e) {
        return new ResponseEntity<>(e.getMessage() + "\n" + e.getApiResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
