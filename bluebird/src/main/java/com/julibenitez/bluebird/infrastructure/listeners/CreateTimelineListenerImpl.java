package com.julibenitez.bluebird.infrastructure.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julibenitez.bluebird.domain.ports.in.CreateUserTimelimeUseCase;
import com.julibenitez.bluebird.domain.ports.in.listeners.CreateTimelineListener;
import com.julibenitez.bluebird.dtos.TweetRequestDto;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class CreateTimelineListenerImpl implements CreateTimelineListener {
    private static final Logger log = LoggerFactory.getLogger(CreateTimelineListenerImpl.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CreateUserTimelimeUseCase createUserTimelimeUseCase;

    public CreateTimelineListenerImpl(CreateUserTimelimeUseCase createUserTimelimeUseCase) {
        this.createUserTimelimeUseCase = createUserTimelimeUseCase;
    }

    @SqsListener("notify-new-tweet")
    public void createTimeline(String message) {
        try {
            TweetRequestDto tweet = objectMapper.readValue(message, TweetRequestDto.class);
            log.info("Received tweet via annotation: " + tweet);

            createUserTimelimeUseCase.execute(tweet);
        } catch (JsonProcessingException e) {
            log.error("Failed to read tweet to SQS: " + e.getMessage());
        }
    }

}
