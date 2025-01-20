package org.kunekune.PiglioTech.exception;

import org.springframework.http.HttpStatus;

public class ApiServiceException extends RuntimeException {
  private String apiResponse;
  private HttpStatus status;

  public ApiServiceException(String apiResponse, String message) {
    super(message);
    this.apiResponse = apiResponse;
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public ApiServiceException(String apiResponse, String message, HttpStatus status) {
    super(message);
    this.apiResponse = apiResponse;
    this.status = status;
  }

  public ApiServiceException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public String getApiResponse() {
    return apiResponse;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
