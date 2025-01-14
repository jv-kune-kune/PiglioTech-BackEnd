package org.kunekune.PiglioTech.exception;

public class ApiServiceException extends RuntimeException {
    private String apiResponse;

    public ApiServiceException(String apiResponse, String message) {
        super(message);
        this.apiResponse = apiResponse;
    }

    public String getApiResponse() {
        return apiResponse;
    }
}
