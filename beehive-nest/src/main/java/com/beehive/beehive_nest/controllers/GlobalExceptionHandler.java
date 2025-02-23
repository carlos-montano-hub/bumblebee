package com.beehive.beehive_nest.controllers;

import com.beehive.beehive_nest.exceptions.DependencyNotFoundException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorResponse("Invalid argument", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DependencyNotFoundException.class)
    public ResponseEntity<Object> handleIllegalArgument(DependencyNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("Dependency not found", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleException(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}

class ErrorResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
