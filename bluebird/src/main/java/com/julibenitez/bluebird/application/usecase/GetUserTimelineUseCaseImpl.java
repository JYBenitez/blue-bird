package com.julibenitez.bluebird.application.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.julibenitez.bluebird.domain.model.UserTimeline;
import com.julibenitez.bluebird.domain.ports.in.GetUserTimelineUseCase;
import com.julibenitez.bluebird.dtos.GetUserTimelineRequestDto;
import com.julibenitez.bluebird.dtos.TimelineResponseDto;
import com.julibenitez.bluebird.dtos.UserTimeLineResponseDto;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserTimelineRepository;

@Component
public class GetUserTimelineUseCaseImpl implements GetUserTimelineUseCase {
    private final UserTimelineRepository userTimelineRepository;

    public GetUserTimelineUseCaseImpl(UserTimelineRepository userTimelineRepository) {
        this.userTimelineRepository = userTimelineRepository;
    }

    public TimelineResponseDto execute(GetUserTimelineRequestDto request) {
        List<UserTimeline> timeline = userTimelineRepository.findTimelineByFollowerIdWithCursor(request.followerId(),
                request.lastSeedtweetId(), PageRequest.of(0, request.limit()));

        List<UserTimeLineResponseDto> tweets = timeline.stream().map(t -> new UserTimeLineResponseDto(
                t.getId(),
                t.getFollowerId(),
                t.getTweetId(),
                t.getAuthorId(),
                t.getContent(),
                t.getCreatedAt()))
                .toList();
        UUID lastSeedTweet = tweets.isEmpty() ? null : tweets.get(tweets.size() - 1).id();
        TimelineResponseDto result = new TimelineResponseDto(tweets, lastSeedTweet);
        return result;
    }
}
