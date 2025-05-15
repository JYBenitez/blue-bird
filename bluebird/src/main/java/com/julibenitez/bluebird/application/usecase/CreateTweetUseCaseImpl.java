package com.julibenitez.bluebird.application.usecase;

import org.springframework.stereotype.Service;

import com.julibenitez.bluebird.domain.ports.in.CreateTweetUseCase;
import com.julibenitez.bluebird.domain.ports.out.TweetRepository;
import com.julibenitez.bluebird.domain.service.CreateTweetServiceImpl;

@Service
public class CreateTweetUseCaseImpl implements CreateTweetUseCase {

    private final CreateTweetServiceImpl createTweetService;
    private final TweetRepository tweetRepository;

    
    public CreateTweetUseCaseImpl(CreateTweetServiceImpl createTweetService, TweetRepository tweetRepository) {
        this.createTweetService = createTweetService;
        this.tweetRepository = tweetRepository;
    }


    @Override
    public void execute(String userId, String content) {
       tweetRepository.save(createTweetService.createTweet(userId, content));
    }

}
