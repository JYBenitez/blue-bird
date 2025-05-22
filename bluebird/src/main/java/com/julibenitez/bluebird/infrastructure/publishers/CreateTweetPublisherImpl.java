package com.julibenitez.bluebird.infrastructure.publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetNotificator;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetPublisher;
import com.julibenitez.bluebird.dtos.TweetRequestDto;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Component
public class CreateTweetPublisherImpl implements NewTweetPublisher {
    private static final Logger log = LoggerFactory.getLogger(CreateTweetPublisherImpl.class);

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final NewTweetNotificator newTweetNotificator;

    public CreateTweetPublisherImpl(SqsTemplate sqsTemplate, NewTweetNotificator newTweetNotificator) {
        this.sqsTemplate = sqsTemplate;
        this.newTweetNotificator = newTweetNotificator;
    }

    @Override
    public void publish(TweetRequestDto tweet) {
        String message;

        try {
            message = objectMapper.writeValueAsString(tweet);
            sqsTemplate.send(to -> to.queue("create-tweet").payload(message));
            log.info("Sent tweet via SqsTemplate: " + tweet);
            // Send notification to propagate tweet to timeline
            newTweetNotificator.publish(tweet);
        } catch (Exception e) {
            log.error("Failed to send tweet to SQS: " + e.getMessage());
            // throw exception
        }
    }
}