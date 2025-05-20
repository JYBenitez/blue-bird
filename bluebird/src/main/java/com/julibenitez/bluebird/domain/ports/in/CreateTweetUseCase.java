package com.julibenitez.bluebird.domain.ports.in;

public interface CreateTweetUseCase {
    void execute(String id, String userId, String content);
}
