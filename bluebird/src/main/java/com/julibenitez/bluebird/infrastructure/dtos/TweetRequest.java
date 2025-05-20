package com.julibenitez.bluebird.infrastructure.dtos;

import java.util.UUID;

public record TweetRequest(
        UUID id,
        String userId,
        String content) {
    public TweetRequest withUserId(String newUserId) {
        return new TweetRequest(id, newUserId, content);
    }

    public TweetRequest withId(UUID newId) {
        return new TweetRequest(newId, userId, content);
    }
}