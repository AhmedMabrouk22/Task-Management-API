package org.example.taskmanagementapi.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)

public class InvalidEmailOrPasswordException extends RuntimeException{
    public InvalidEmailOrPasswordException() {
        super("Invalid email or password");
    }

}
