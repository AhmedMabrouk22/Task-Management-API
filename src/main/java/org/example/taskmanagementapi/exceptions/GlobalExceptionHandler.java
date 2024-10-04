package org.example.taskmanagementapi.exceptions;


import org.apache.juli.logging.Log;
import org.example.taskmanagementapi.utils.ApiResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotFoundExceptionHandler.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundExceptionHandler ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.response(false,ex.getMessage(),null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handelValidationException(MethodArgumentNotValidException ex) {
        var error = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.response(false,error,null));
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<String>> handelDatabaseException(DatabaseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.response(false,ex.getMessage(),null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handelGlobalException(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.response(false,"An unexpected error occurred",null));
    }
}
