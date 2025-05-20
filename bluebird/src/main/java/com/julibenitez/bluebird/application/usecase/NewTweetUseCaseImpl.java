package com.julibenitez.bluebird.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.julibenitez.bluebird.domain.ports.in.NewTweetUseCase;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetPublisher;
import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.dtos.TweetResponseDto;

@Component
public class NewTweetUseCaseImpl implements NewTweetUseCase {
    private NewTweetPublisher publisher;

    public NewTweetUseCaseImpl(NewTweetPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public TweetResponseDto execute(TweetRequestDto request) {
        UUID id = UUID.randomUUID();
        publisher.publish(request.withId(id));

        return new TweetResponseDto(id.toString());
    }

}
