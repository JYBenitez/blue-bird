package com.julibenitez.bluebird.infrastructure.publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetNotificator;
import com.julibenitez.bluebird.dtos.TweetRequestDto;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Component
public class NewTweetNotificatorImpl implements NewTweetNotificator {
    private static final Logger log = LoggerFactory.getLogger(NewTweetNotificatorImpl.class);

    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewTweetNotificatorImpl(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void publish(TweetRequestDto tweet) {
        String message;

        try {
            message = objectMapper.writeValueAsString(tweet);
            sqsTemplate.send(to -> to.queue("notify-new-tweet").payload(message));
            log.info("Sent tweet via SqsTemplate: " + tweet);
        } catch (Exception e) {
            log.error("Failed to send tweet to SQS: " + e.getMessage());
            // throw exception
        }
    }
}
