package com.julibenitez.bluebird.domain.exceptions;

public class InputNotValidException  extends CustomException {

    public InputNotValidException(String code, String message) {
        super(code, message);
    }
}

