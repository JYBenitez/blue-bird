package com.julibenitez.bluebird.application.usecases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import com.julibenitez.bluebird.application.usecase.NewTweetUseCaseImpl;
import com.julibenitez.bluebird.domain.ports.out.publishers.NewTweetPublisher;
import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.dtos.TweetResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class NewTweetUseCaseTest {
    private NewTweetPublisher publisher;
    private NewTweetUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        publisher = mock(NewTweetPublisher.class);
        useCase = new NewTweetUseCaseImpl(publisher);
    }

    @Test
    void testExecute_PublishesTweetWithNewId_AndReturnsResponse() {
        TweetRequestDto request = mock(TweetRequestDto.class);
        TweetRequestDto requestWithId = mock(TweetRequestDto.class);
        when(request.withId(any(UUID.class))).thenReturn(requestWithId);

        TweetResponseDto response = useCase.execute(request);

        verify(publisher, times(1)).publish(requestWithId);
        assertNotNull(response.id());
    }

    @Test
    void testExecute_ResponseIdMatchesPublishedId() {
        TweetRequestDto request = mock(TweetRequestDto.class);
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);

        // Simula que el método withId devuelve un nuevo DTO con el mismo UUID
        TweetRequestDto requestWithId = mock(TweetRequestDto.class);
        when(request.withId(any(UUID.class))).thenReturn(requestWithId);

        TweetResponseDto response = useCase.execute(request);

        // Captura el UUID usado en withId
        verify(request).withId(uuidCaptor.capture());
        UUID usedId = uuidCaptor.getValue();

        // El response debe tener el mismo ID
        assertEquals(usedId.toString(), response.id());
    }

    @Test
    void testExecute_NullRequest_ThrowsExceptionAndDoesNotPublish() {
        assertThrows(com.julibenitez.bluebird.domain.exceptions.InputNotValidException.class, () -> {
            useCase.execute(null);
        });
        verifyNoInteractions(publisher);
    }
}
