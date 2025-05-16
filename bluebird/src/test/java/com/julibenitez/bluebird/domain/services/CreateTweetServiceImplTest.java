package com.julibenitez.bluebird.domain.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.julibenitez.bluebird.domain.exceptions.TweetEmptyException;
import com.julibenitez.bluebird.domain.exceptions.TweetExceedsLengthException;
import com.julibenitez.bluebird.domain.model.Tweet;

class CreateTweetServiceImplTest {

    private CreateTweetServiceImpl createTweetService;

    @BeforeEach
    void setUp() {
        createTweetService = new CreateTweetServiceImpl();
    }

    @Test
    @DisplayName("Should create tweet successfully with valid content")
    void createTweet_Success() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        String content = "This is a valid tweet";

        // Act
        Tweet tweet = createTweetService.createTweet(userId, content);

        // Assert
        assertNotNull(tweet);
        assertEquals(userId, tweet.getUserId().toString());
        assertEquals(content, tweet.getContent());
        assertNotNull(tweet.getId());
        assertNotNull(tweet.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw TweetExceedsLengthException when content exceeds 280 characters")
    void createTweet_ExceedsLength() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        String content = "a".repeat(281);

        // Act & Assert
        TweetExceedsLengthException exception = assertThrows(
            TweetExceedsLengthException.class,
            () -> createTweetService.createTweet(userId, content)
        );

        assertEquals("TWEET_EXCEEDS_LENGTH", exception.getCode());
        assertEquals("Content cannot exceed 280 characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw TweetEmptyException when content is empty")
    void createTweet_EmptyContent() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        String content = "";

        // Act & Assert
        TweetEmptyException exception = assertThrows(
            TweetEmptyException.class,
            () -> createTweetService.createTweet(userId, content)
        );

        assertEquals("TWEET_EMPTY", exception.getCode());
        assertEquals("Content cannot empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw TweetEmptyException when content is blank")
    void createTweet_BlankContent() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        String content = "   ";

        // Act & Assert
        TweetEmptyException exception = assertThrows(
            TweetEmptyException.class,
            () -> createTweetService.createTweet(userId, content)
        );

        assertEquals("TWEET_EMPTY", exception.getCode());
        assertEquals("Content cannot empty", exception.getMessage());
    }
} 