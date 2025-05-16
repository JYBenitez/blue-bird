package com.julibenitez.bluebird.domain.exceptions;

public class TweetEmptyException extends CustomException {

    public TweetEmptyException(String code, String message) {
        super(code, message);
    }
}
