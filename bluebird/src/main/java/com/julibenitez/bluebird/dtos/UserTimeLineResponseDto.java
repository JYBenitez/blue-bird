package com.julibenitez.bluebird.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserTimeLineResponseDto(
        UUID id,
        UUID followerId,
        UUID tweetId,
        UUID authorId,
        String content,
        LocalDateTime createdAt) {
}
