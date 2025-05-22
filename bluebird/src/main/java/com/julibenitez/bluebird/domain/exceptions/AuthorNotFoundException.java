package com.julibenitez.bluebird.domain.exceptions;

public class AuthorNotFoundException extends CustomException {

    public AuthorNotFoundException(String code, String message) {
        super(code, message);
    }
}
