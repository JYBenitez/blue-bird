package com.julibenitez.bluebird.application.usecases;

import static org.mockito.Mockito.*;

import java.util.*;

import com.julibenitez.bluebird.application.usecase.CreateUserTimelimeUseCaseImpl;
import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;
import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.FollowRepository;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserRepository;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserTimelineRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Assertions;

public class CreateUserTimelimeUseCaseTest {
    @Mock
    private UserTimelineRepository userTimelineRepository;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserTimelimeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateUserTimelimeUseCaseImpl(userTimelineRepository, followRepository, userRepository);
    }

    @Test
    void testExecute_PropagatesTweetToFollowers() {
        UUID authorId = UUID.randomUUID();
        UUID tweetId = UUID.randomUUID();
        User author = mock(User.class);
        when(author.getId()).thenReturn(authorId);
        TweetRequestDto tweetRequestDto = mock(TweetRequestDto.class);
        when(tweetRequestDto.userId()).thenReturn(authorId.toString());
        when(tweetRequestDto.content()).thenReturn("Hello World");
        when(tweetRequestDto.id()).thenReturn(tweetId);

        Follow follower1 = mock(Follow.class);
        Follow follower2 = mock(Follow.class);
        when(follower1.getId()).thenReturn(UUID.randomUUID());
        when(follower2.getId()).thenReturn(UUID.randomUUID());
        List<Follow> followers = Arrays.asList(follower1, follower2);

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(followRepository.findByFollower(author)).thenReturn(followers);

        useCase.execute(tweetRequestDto);

        verify(userTimelineRepository, atLeastOnce()).saveAll(anyList());
        verify(userTimelineRepository, atLeastOnce()).flush();
    }

    @Test
    void testExecute_AuthorNotFound_ThrowsException() {
        UUID authorId = UUID.randomUUID();
        TweetRequestDto tweetRequestDto = mock(TweetRequestDto.class);
        when(tweetRequestDto.userId()).thenReturn(authorId.toString());
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        Assertions.assertThrows(com.julibenitez.bluebird.domain.exceptions.AuthorNotFoundException.class, () -> {
            useCase.execute(tweetRequestDto);
        });

        verifyNoInteractions(followRepository);
        verifyNoInteractions(userTimelineRepository);
    }

    @Test
    void testExecute_NoFollowers_DoesNothing() {
        UUID authorId = UUID.randomUUID();
        User author = mock(User.class);
        when(author.getId()).thenReturn(authorId);
        TweetRequestDto tweetRequestDto = mock(TweetRequestDto.class);
        when(tweetRequestDto.userId()).thenReturn(authorId.toString());
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(followRepository.findByFollower(author)).thenReturn(Collections.emptyList());

        useCase.execute(tweetRequestDto);

        verify(userTimelineRepository, never()).saveAll(anyList());
        verify(userTimelineRepository, never()).flush();
    }

    @Test
    void testExecute_BatchSaveCalledMultipleTimes_WhenFollowersExceedBatchSize() {
        UUID authorId = UUID.randomUUID();
        UUID tweetId = UUID.randomUUID();
        User author = mock(User.class);
        when(author.getId()).thenReturn(authorId);
        TweetRequestDto tweetRequestDto = mock(TweetRequestDto.class);
        when(tweetRequestDto.userId()).thenReturn(authorId.toString());
        when(tweetRequestDto.content()).thenReturn("Batch test");
        when(tweetRequestDto.id()).thenReturn(tweetId);

        // Crear 250 followers
        List<Follow> followers = new ArrayList<>();
        for (int i = 0; i < 250; i++) {
            Follow follower = mock(Follow.class);
            when(follower.getId()).thenReturn(UUID.randomUUID());
            followers.add(follower);
        }

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(followRepository.findByFollower(author)).thenReturn(followers);

        useCase.execute(tweetRequestDto);

        // Debe llamar 3 veces (100 + 100 + 50)
        verify(userTimelineRepository, times(3)).saveAll(anyList());
        verify(userTimelineRepository, times(3)).flush();
    }

    @Test
    void testExecute_InvalidUserId_ThrowsException() {
        String invalidUserId = "not-a-uuid";
        TweetRequestDto tweetRequestDto = mock(TweetRequestDto.class);
        when(tweetRequestDto.userId()).thenReturn(invalidUserId);

        Assertions.assertThrows(com.julibenitez.bluebird.domain.exceptions.UserIdNotValidException.class, () -> {
            useCase.execute(tweetRequestDto);
        });

        verifyNoInteractions(userRepository);
        verifyNoInteractions(followRepository);
        verifyNoInteractions(userTimelineRepository);
    }

    @Test
    void testNoTimelineSaved_WhenFollowersListIsEmpty() {
        UUID authorId = UUID.randomUUID();
        User author = mock(User.class);
        when(author.getId()).thenReturn(authorId);
        TweetRequestDto tweetRequestDto = mock(TweetRequestDto.class);
        when(tweetRequestDto.userId()).thenReturn(authorId.toString());
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(followRepository.findByFollower(author)).thenReturn(Collections.emptyList());

        useCase.execute(tweetRequestDto);

        verify(userTimelineRepository, never()).saveAll(anyList());
        verify(userTimelineRepository, never()).flush();
    }
}
