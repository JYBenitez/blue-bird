package com.julibenitez.bluebird.infrastructure.web.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julibenitez.bluebird.domain.ports.in.NewTweetUseCase;
import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.dtos.TweetResponseDto;

@RestController
@RequestMapping("api/v1/tweets")
public class TweetController {
    private final NewTweetUseCase newTweetUseCase;

    public TweetController(
            NewTweetUseCase newTweetUseCase) {
        this.newTweetUseCase = newTweetUseCase;
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<TweetResponseDto> postTweet(
            @PathVariable("user_id") String userId,
            @RequestBody TweetRequestDto request) {
        TweetResponseDto responseDto = newTweetUseCase.execute(request.withUserId(userId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);

    }
}