package com.julibenitez.bluebird.domain.ports.out.publishers;

import com.julibenitez.bluebird.dtos.TweetRequestDto;

public interface NewTweetPublisher {
    void publish(TweetRequestDto message);
}
