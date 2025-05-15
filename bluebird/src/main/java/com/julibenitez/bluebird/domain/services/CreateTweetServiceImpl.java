package com.julibenitez.bluebird.domain.service;

import org.apache.logging.log4j.util.Strings;

import com.julibenitez.bluebird.domain.model.Tweet;
import com.julibenitez.bluebird.domain.ports.in.CreateTweetService;

public class CreateTweetServiceImpl implements CreateTweetService{

    public Tweet createTweet(String userId, String content) {
        validate(content);

        return builderTweet(userId, content);
    }

    private boolean validate(String content) {
        return  Strings.isNotBlank(content) && content.length() < 280;
    }

    private Tweet builderTweet(String userId, String content) {
        return new Tweet.Builder()
        .userId(userId)
        .content(content)
        .createdAtDefault()
        .newId()
        .build();
    }
}
