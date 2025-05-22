package com.julibenitez.bluebird.application.usecases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.julibenitez.bluebird.application.usecase.GetUserTimelineUseCaseImpl;
import com.julibenitez.bluebird.domain.model.UserTimeline;
import com.julibenitez.bluebird.dtos.GetUserTimelineRequestDto;
import com.julibenitez.bluebird.dtos.TimelineResponseDto;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserTimelineRepository;
import com.julibenitez.bluebird.domain.exceptions.LimitNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserTimelineUseCaseTest {

    @Mock
    private UserTimelineRepository userTimelineRepository;

    @InjectMocks
    private GetUserTimelineUseCaseImpl useCase;

    @Test
    void execute_returnsTimelineResponseDtoWithTweets() {
        // Arrange
        UUID followerId = UUID.randomUUID();
        UUID lastSeenTweetId = UUID.randomUUID();
        int limit = 2;
        GetUserTimelineRequestDto request = new GetUserTimelineRequestDto(followerId, lastSeenTweetId, limit);
     
        List<UserTimeline> mockTimeline = List.of(new UserTimeline.Builder()
            .authorId(UUID.randomUUID())
            .content("Tweet 1")
            .createdAt(LocalDateTime.now())
            .followerId(followerId)
            .newId()
            .build(),
        new UserTimeline.Builder()
            .authorId(UUID.randomUUID())
            .content("Tweet 2")
            .createdAt(LocalDateTime.now())
            .followerId(followerId)       
            .newId()
            .build()
        );

        when(userTimelineRepository.findTimelineByFollowerIdWithCursor(eq(followerId), eq(lastSeenTweetId), any(PageRequest.class)))
            .thenReturn(mockTimeline);

        // Act
        TimelineResponseDto response = useCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.timeline().size());
        assertEquals(mockTimeline.get(1).getId(), response.lastSeedTweet());
        verify(userTimelineRepository).findTimelineByFollowerIdWithCursor(eq(followerId), eq(lastSeenTweetId), any(PageRequest.class));
    }
    @Test
    void execute_returnsEmptyTimelineWhenNoTweets() {
        UUID followerId = UUID.randomUUID();
        GetUserTimelineRequestDto request = new GetUserTimelineRequestDto(followerId, null, 10);
    
        when(userTimelineRepository.findTimelineByFollowerIdWithCursor(eq(followerId), eq(null), any(PageRequest.class)))
            .thenReturn(List.of());
    
        TimelineResponseDto response = useCase.execute(request);
    
        assertNotNull(response);
        assertEquals(0, response.timeline().size());
        assertEquals(null, response.lastSeedTweet());
    }
    
    @Test
    void execute_throwsExceptionWhenLimitIsZero() {
        UUID followerId = UUID.randomUUID();
        int limit = 0;
        GetUserTimelineRequestDto request = new GetUserTimelineRequestDto(followerId, null, limit);

        // No es necesario mockear el repositorio porque debe lanzar antes la excepción
        assertThrows(LimitNotValidException.class, () -> useCase.execute(request));
    }

    @Test
    void execute_propagatesExceptionWhenRepositoryFails() {
        UUID followerId = UUID.randomUUID();
        int limit = 2;
        GetUserTimelineRequestDto request = new GetUserTimelineRequestDto(followerId, null, limit);

        RuntimeException repositoryException = new RuntimeException("Database error");
        when(userTimelineRepository.findTimelineByFollowerIdWithCursor(eq(followerId), eq(null), any(PageRequest.class)))
            .thenThrow(repositoryException);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> useCase.execute(request));
        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void execute_mapsUserTimelineFieldsCorrectly() {
        UUID followerId = UUID.randomUUID();
        UUID tweetId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID timelineId = UUID.randomUUID();
        String content = "Contenido de prueba";
        LocalDateTime createdAt = LocalDateTime.now();
        int limit = 1;
        GetUserTimelineRequestDto request = new GetUserTimelineRequestDto(followerId, null, limit);

        UserTimeline userTimeline = new UserTimeline.Builder()
            .id(timelineId)
            .followerId(followerId)
            .tweetId(tweetId)
            .authorId(authorId)
            .content(content)
            .createdAt(createdAt)
            .build();

        when(userTimelineRepository.findTimelineByFollowerIdWithCursor(eq(followerId), eq(null), any(PageRequest.class)))
            .thenReturn(List.of(userTimeline));

        TimelineResponseDto response = useCase.execute(request);

        assertNotNull(response);
        assertEquals(1, response.timeline().size());
        var dto = response.timeline().get(0);
        assertEquals(timelineId, dto.id());
        assertEquals(followerId, dto.followerId());
        assertEquals(tweetId, dto.tweetId());
        assertEquals(authorId, dto.authorId());
        assertEquals(content, dto.content());
        assertEquals(createdAt, dto.createdAt());
    }
}
