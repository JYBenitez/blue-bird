package com.julibenitez.bluebird.domain.ports.in;

import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.dtos.TweetResponseDto;

public interface NewTweetUseCase {
    TweetResponseDto execute(TweetRequestDto request);
}
