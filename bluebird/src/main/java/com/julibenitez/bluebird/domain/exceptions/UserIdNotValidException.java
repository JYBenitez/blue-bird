package com.julibenitez.bluebird.domain.exceptions;

public class UserIdNotValidException extends CustomException {
    public UserIdNotValidException(String code,String message) {
        super(code,message);
    }
}
