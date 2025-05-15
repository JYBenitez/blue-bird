package com.julibenitez.bluebird.domain.ports.in;

public interface CreateTweetUseCase {
    void execute(String userId, String content);
}
