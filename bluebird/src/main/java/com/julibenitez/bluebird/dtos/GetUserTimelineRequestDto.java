package com.julibenitez.bluebird.dtos;

import java.util.UUID;

public record GetUserTimelineRequestDto(
        UUID followerId,
        UUID lastSeenTweetId,
        int limit) {
    public GetUserTimelineRequestDto withFollowerId(UUID newFollowerId) {
        return new GetUserTimelineRequestDto(newFollowerId, lastSeenTweetId, limit);
    }
}
