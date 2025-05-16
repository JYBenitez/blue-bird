package com.julibenitez.bluebird.infrastructure.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.julibenitez.bluebird.domain.exceptions.CustomException;
import com.julibenitez.bluebird.domain.exceptions.TweetEmptyException;
import com.julibenitez.bluebird.domain.exceptions.TweetExceedsLengthException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { TweetExceedsLengthException.class, TweetEmptyException.class })
    public ResponseEntity<ApiError> handleTweetExceptions(CustomException ex) {
        ApiError error = new ApiError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
