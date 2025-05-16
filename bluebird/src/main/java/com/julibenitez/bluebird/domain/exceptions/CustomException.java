package com.julibenitez.bluebird.domain.exceptions;

public abstract class CustomException extends RuntimeException {
    private final String code;
    private final String message;

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
