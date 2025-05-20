package com.julibenitez.bluebird.domain.ports.in;

import com.julibenitez.bluebird.domain.model.Tweet;

public interface CreateTweetService {
    Tweet createTweet(String id, String userId, String content);
}
