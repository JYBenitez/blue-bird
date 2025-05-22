package com.julibenitez.bluebird.domain.ports.in;

import com.julibenitez.bluebird.dtos.TweetRequestDto;

public interface CreateUserTimelimeUseCase {
    void execute(TweetRequestDto tweetRequestDto);
}
