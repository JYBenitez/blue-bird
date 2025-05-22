package com.julibenitez.bluebird.domain.exceptions;

public class LimitNotValidException extends CustomException{
    public LimitNotValidException(String code, String message) {
        super(code, message);
    }
}
