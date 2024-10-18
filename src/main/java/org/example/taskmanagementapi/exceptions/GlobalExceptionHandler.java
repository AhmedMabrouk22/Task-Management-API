package org.example.taskmanagementapi.exceptions;


import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.exceptions.auth.InvalidEmailOrPasswordException;
import org.example.taskmanagementapi.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundExceptionHandler ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.response(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handelValidationException(MethodArgumentNotValidException ex) {
        var error = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.response(error));
    }

    // Database
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handelDatabaseException(DatabaseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.response(ex.getMessage()));
    }

    // Auth
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handelAuthException(AuthException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ErrorResponse.response(ex.getMessage()));
    }
    @ExceptionHandler(InvalidEmailOrPasswordException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(InvalidEmailOrPasswordException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.response(ex.getMessage()));
    }




    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.response(ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.response("this endpoint not exist"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handelGlobalException(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.response("An unexpected error occurred"));
    }
}
