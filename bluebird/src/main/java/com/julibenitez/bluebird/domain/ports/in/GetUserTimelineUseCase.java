package com.julibenitez.bluebird.domain.ports.in;

import com.julibenitez.bluebird.dtos.GetUserTimelineRequestDto;
import com.julibenitez.bluebird.dtos.TimelineResponseDto;

public interface GetUserTimelineUseCase {
    TimelineResponseDto execute(GetUserTimelineRequestDto request);
}
