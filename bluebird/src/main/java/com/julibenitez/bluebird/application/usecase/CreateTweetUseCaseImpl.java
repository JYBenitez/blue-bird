package com.julibenitez.bluebird.application.usecase;

import org.springframework.stereotype.Service;

import com.julibenitez.bluebird.domain.model.Tweet;
import com.julibenitez.bluebird.domain.ports.in.CreateTweetService;
import com.julibenitez.bluebird.domain.ports.in.CreateTweetUseCase;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.TweetRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateTweetUseCaseImpl implements CreateTweetUseCase {

    private final CreateTweetService createTweetService;
    private final TweetRepository tweetRepository;

    public CreateTweetUseCaseImpl(CreateTweetService createTweetService, TweetRepository tweetRepository) {
        this.createTweetService = createTweetService;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public void execute(String id, String userId, String content) {
        Tweet tweet = createTweetService.createTweet(id, userId, content);
        tweetRepository.save(tweet);
    }

}
