package com.julibenitez.bluebird.domain.ports.in;

import com.julibenitez.bluebird.infrastructure.dtos.TweetRequest;
import com.julibenitez.bluebird.infrastructure.dtos.TweetResponseDto;

public interface NewTweetUseCase {
    TweetResponseDto execute(TweetRequest request);
}
