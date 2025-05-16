package com.julibenitez.bluebird.infrastructure.web.controllers;

public class ApiError {
    private String code;
    private String message;
    private String timestamp;

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = java.time.ZonedDateTime.now().toString();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
