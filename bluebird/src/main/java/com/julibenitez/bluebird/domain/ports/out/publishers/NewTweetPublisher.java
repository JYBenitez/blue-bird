package com.julibenitez.bluebird.domain.ports.out.publishers;

import com.julibenitez.bluebird.infrastructure.dtos.TweetRequest;

public interface NewTweetPublisher {
    void publish(TweetRequest message);
}
