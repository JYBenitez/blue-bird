package com.julibenitez.bluebird.infrastructure.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julibenitez.bluebird.domain.ports.in.CreateTweetUseCase;
import com.julibenitez.bluebird.infrastructure.dtos.TweetRequest;

@RestController
@RequestMapping("/tweets")
public class TweetController {
    private final CreateTweetUseCase createTweetUseCase;

    public TweetController(CreateTweetUseCase createTweetUseCase) {
        this.createTweetUseCase = createTweetUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> postTweet(@RequestBody TweetRequest request) {
        createTweetUseCase.execute(request.userId(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}