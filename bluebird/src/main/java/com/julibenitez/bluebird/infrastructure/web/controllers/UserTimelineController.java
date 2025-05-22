package com.julibenitez.bluebird.infrastructure.web.controllers;

import java.util.UUID;

import org.hibernate.mapping.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.julibenitez.bluebird.domain.ports.in.GetUserTimelineUseCase;
import com.julibenitez.bluebird.dtos.GetUserTimelineRequestDto;
import com.julibenitez.bluebird.dtos.TimelineResponseDto;

@RestController
@RequestMapping("api/v1/timeline")
public class UserTimelineController {
    private final GetUserTimelineUseCase getUserTimelineUseCase;

    public UserTimelineController(GetUserTimelineUseCase getUserTimelineUseCase) {
        this.getUserTimelineUseCase = getUserTimelineUseCase;
    }

    @GetMapping("/{follower_id}")
    public ResponseEntity<TimelineResponseDto> getTimeline(
            @PathVariable("follower_id") UUID followerId,
            @RequestParam(required = false) UUID lastSeedTweet,
            @RequestParam(defaultValue = "20") int limit) {
        var responseDto = getUserTimelineUseCase
                .execute(new GetUserTimelineRequestDto(followerId, lastSeedTweet, limit));
        return ResponseEntity.ok(responseDto);

    }
}
