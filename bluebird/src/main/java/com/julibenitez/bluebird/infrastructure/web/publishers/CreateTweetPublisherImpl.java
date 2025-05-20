package com.julibenitez.bluebird.infrastructure.web.publishers;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetPublisher;
import com.julibenitez.bluebird.dtos.TweetRequestDto;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Component
public class CreateTweetPublisherImpl implements NewTweetPublisher {
    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CreateTweetPublisherImpl(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void publish(TweetRequestDto tweet) {
        String message;

        try {
            message = objectMapper.writeValueAsString(tweet);
            sqsTemplate.send(to -> to.queue("create-tweet").payload(message));
            System.out.println("Sent tweet via SqsTemplate: " + tweet);
        } catch (Exception e) {
            System.err.println("Failed to send tweet to SQS: " + e.getMessage());
            // throw exception
        }
    }
}