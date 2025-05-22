package com.julibenitez.bluebird.infrastructure.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julibenitez.bluebird.domain.ports.in.CreateTweetUseCase;
import com.julibenitez.bluebird.domain.ports.in.listeners.NewTweetListener;
import com.julibenitez.bluebird.dtos.TweetRequestDto;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class CreateTweetListenerImpl implements NewTweetListener {
    private static final Logger log = LoggerFactory.getLogger(CreateTweetListenerImpl.class);

    private final CreateTweetUseCase createTweetUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CreateTweetListenerImpl(CreateTweetUseCase createTweetUseCase) {
        this.createTweetUseCase = createTweetUseCase;
    }

    @SqsListener("create-tweet")
    public void createTweet(String message) {

        try {
            TweetRequestDto tweet = objectMapper.readValue(message, TweetRequestDto.class);
            log.info("📥 Received tweet via annotation: " + tweet);

            createTweetUseCase.execute(tweet.id().toString(), tweet.userId(), tweet.content());
        } catch (JsonProcessingException e) {
            log.error("Failed to read tweet to SQS: " + e.getMessage());
        }

    }
}
