package com.julibenitez.bluebird.domain.services;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.julibenitez.bluebird.domain.exceptions.TweetEmptyException;
import com.julibenitez.bluebird.domain.exceptions.TweetExceedsLengthException;
import com.julibenitez.bluebird.domain.model.Tweet;
import com.julibenitez.bluebird.domain.ports.in.CreateTweetService;

@Service
public class CreateTweetServiceImpl implements CreateTweetService {
    private final String TWEET_EXCEEDS_LENGTH = "Content cannot exceed 280 characters";
    private final String TWEET_EXCEEDS_LENGTH_CODE = "TWEET_EXCEEDS_LENGTH";
    private final String TWEET_EMPTY = "Content cannot empty";
    private final String TWEET_EMPTY_CODE = "TWEET_EMPTY";
    private final Integer TWEET_MAX_EXCEEDS_LENGTH = 280;

    public Tweet createTweet(String id, String userId, String content) {
        validate(content);

        return builderTweet(id, userId, content);
    }

    private void validate(String content) {
        if (content.length() > TWEET_MAX_EXCEEDS_LENGTH)
            throw new TweetExceedsLengthException(TWEET_EXCEEDS_LENGTH_CODE, TWEET_EXCEEDS_LENGTH);
        if (Strings.isBlank(content))
            throw new TweetEmptyException(TWEET_EMPTY_CODE, TWEET_EMPTY);
    }

    private Tweet builderTweet(String id, String userId, String content) {
        return new Tweet.Builder()
                .userId(userId)
                .content(content)
                .createdAtDefault()
                .id(id)
                .build();
    }
}
