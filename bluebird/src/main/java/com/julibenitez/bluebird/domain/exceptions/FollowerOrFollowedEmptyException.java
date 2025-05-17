package com.julibenitez.bluebird.domain.exceptions;

public class FollowerOrFollowedEmptyException extends CustomException {

    public FollowerOrFollowedEmptyException(String code, String message) {
        super(code, message);
    }
}
