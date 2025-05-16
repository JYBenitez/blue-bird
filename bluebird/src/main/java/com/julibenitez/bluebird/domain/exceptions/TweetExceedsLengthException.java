package com.julibenitez.bluebird.domain.exceptions;

public class TweetExceedsLengthException extends CustomException {

    public TweetExceedsLengthException(String code, String message) {
        super(code, message);
    }

}
