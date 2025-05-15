package com.julibenitez.bluebird.domain.ports.in;

import com.julibenitez.bluebird.domain.model.Tweet;

public interface CreateTweetService {
    Tweet createTweet(String userId, String content);
}
