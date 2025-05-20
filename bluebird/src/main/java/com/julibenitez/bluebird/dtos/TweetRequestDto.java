package com.julibenitez.bluebird.dtos;

import java.util.UUID;

public record TweetRequestDto(
        UUID id,
        String userId,
        String content) {
    public TweetRequestDto withUserId(String newUserId) {
        return new TweetRequestDto(id, newUserId, content);
    }

    public TweetRequestDto withId(UUID newId) {
        return new TweetRequestDto(newId, userId, content);
    }
}