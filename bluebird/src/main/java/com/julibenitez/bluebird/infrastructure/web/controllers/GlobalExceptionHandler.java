package com.julibenitez.bluebird.infrastructure.web.controllers;

import java.rmi.UnexpectedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.julibenitez.bluebird.domain.exceptions.CustomException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<ApiError> handleTweetExceptions(CustomException ex) {
        ApiError error = new ApiError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<ApiError> handleGeneralExceptions(RuntimeException ex) {
        ApiError error = new ApiError("INTERNAL_SERVER_ERROR", "Couldn't process request");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ApiError> handleGeneralExceptions(Exception ex) {
        ApiError error = new ApiError("INTERNAL_SERVER_ERROR", "An error occurs.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
