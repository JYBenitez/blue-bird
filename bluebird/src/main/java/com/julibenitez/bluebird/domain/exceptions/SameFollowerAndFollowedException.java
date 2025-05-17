package com.julibenitez.bluebird.domain.exceptions;

public class SameFollowerAndFollowedException extends CustomException {

    public SameFollowerAndFollowedException(String code, String message) {
        super(code, message);
    }
}
