package org.example.taskmanagementapi.exceptions.auth;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private HttpStatus statusCode;
    public AuthException(String message,HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
