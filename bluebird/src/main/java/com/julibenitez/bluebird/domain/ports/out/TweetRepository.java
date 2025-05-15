package com.julibenitez.bluebird.domain.ports.out;

import java.util.List;

import com.julibenitez.bluebird.domain.model.Tweet;

public interface TweetRepository {
    void save(Tweet tweet);
    List<Tweet> findTimelineByUserId(String userId);
}
