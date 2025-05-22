package com.julibenitez.bluebird.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.julibenitez.bluebird.domain.exceptions.InputNotValidException;
import com.julibenitez.bluebird.domain.ports.in.NewTweetUseCase;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetPublisher;
import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.dtos.TweetResponseDto;

@Component
public class NewTweetUseCaseImpl implements NewTweetUseCase {
    private static final String INPUT_NOT_VALID = "Input type [{}] is not valid.";
    private static final String INPUT_NOT_VALID_CODE = "INPUT_NOT_VALID";
    private NewTweetPublisher publisher;

    public NewTweetUseCaseImpl(NewTweetPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public TweetResponseDto execute(TweetRequestDto request) {
        if (request == null) {
            String errorMessage = String.format(INPUT_NOT_VALID, TweetRequestDto.class.getSimpleName());
            throw new InputNotValidException(INPUT_NOT_VALID_CODE, errorMessage);
        }

        UUID id = UUID.randomUUID();
        publisher.publish(request.withId(id));

        return new TweetResponseDto(id.toString());
    }

}
