package com.julibenitez.bluebird.dtos;

import java.util.List;
import java.util.UUID;

public record TimelineResponseDto(
        List<UserTimeLineResponseDto> timeline,
        UUID lastSeedTweet) {

}
