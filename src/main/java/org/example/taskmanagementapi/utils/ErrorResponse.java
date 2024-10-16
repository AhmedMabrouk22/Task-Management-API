package org.example.taskmanagementapi.utils;

public class ErrorResponse {
    private boolean status;
    private String message;

    public ErrorResponse() {
        this.status = false;
    }

    public ErrorResponse(String message) {
        this.message = message;
        this.status = false;
    }


    public static ErrorResponse response(String message) {
        return new ErrorResponse(message);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
