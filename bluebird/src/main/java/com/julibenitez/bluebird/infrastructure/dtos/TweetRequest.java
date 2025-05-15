package com.julibenitez.bluebird.infrastructure.dtos;

public record TweetRequest(
    String userId,
    String content
) {}