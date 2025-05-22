package com.julibenitez.bluebird.dtos;

import java.util.UUID;

public record GetUserTimelineRequestDto(
        UUID followerId,
        UUID lastSeedtweetId,
        int limit) {
    public GetUserTimelineRequestDto withFollowerId(UUID newFollowerId) {
        return new GetUserTimelineRequestDto(newFollowerId, lastSeedtweetId, limit);
    }

}
