package com.julibenitez.bluebird.domain.ports.out.publishers;

import com.julibenitez.bluebird.dtos.TweetRequestDto;

public interface NewTweetNotificator {
    void publish(TweetRequestDto tweet);
}
