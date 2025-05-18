package com.julibenitez.bluebird.domain.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.julibenitez.bluebird.domain.exceptions.FollowerOrFollowedEmptyException;
import com.julibenitez.bluebird.domain.exceptions.SameFollowerAndFollowedException;
import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;

class CreateFollowerRelationshipImplTest {

    private CreateFollowerRelationshipImpl createFollowerRelationship;
    private User follower;
    private User followed;

    @BeforeEach
    void setUp() {
        createFollowerRelationship = new CreateFollowerRelationshipImpl();
        follower = new User.Builder()
                .newId()
                .userName("Follower User")
                .build();
        followed = new User.Builder()
                .newId()
                .userName("Followed User")
                .build();
    }

    @Test
    @DisplayName("Should create follower relationship successfully")
    void createRelationship_Success() {
        // Arrange
        Optional<User> optionalFollower = Optional.of(follower);
        Optional<User> optionalFollowed = Optional.of(followed);

        // Act
        Follow follow = createFollowerRelationship.createRelationship(optionalFollower, optionalFollowed);

        // Assert
        assertNotNull(follow);
        assertEquals(follower, follow.getFollower());
        assertEquals(followed, follow.getFollowed());
        assertNotNull(follow.getId());
        assertNotNull(follow.getCreatedAt());
    }

    @Test
    @DisplayName("Should throw FollowerOrFollowedEmptyException when follower is empty")
    void createRelationship_EmptyFollower() {
        // Arrange
        Optional<User> emptyFollower = Optional.empty();
        Optional<User> optionalFollowed = Optional.of(followed);

        // Act & Assert
        FollowerOrFollowedEmptyException exception = assertThrows(
            FollowerOrFollowedEmptyException.class,
            () -> createFollowerRelationship.createRelationship(emptyFollower, optionalFollowed)
        );

        assertEquals("FOLLOWER_OR_FOLLOWED_EMPTY", exception.getCode());
        assertEquals("Follower or followed couldn't empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw FollowerOrFollowedEmptyException when followed is empty")
    void createRelationship_EmptyFollowed() {
        // Arrange
        Optional<User> optionalFollower = Optional.of(follower);
        Optional<User> emptyFollowed = Optional.empty();

        // Act & Assert
        FollowerOrFollowedEmptyException exception = assertThrows(
            FollowerOrFollowedEmptyException.class,
            () -> createFollowerRelationship.createRelationship(optionalFollower, emptyFollowed)
        );

        assertEquals("FOLLOWER_OR_FOLLOWED_EMPTY", exception.getCode());
        assertEquals("Follower or followed couldn't empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw SameFollowerAndFollowedException when follower and followed are the same user")
    void createRelationship_SameUser() {
        // Arrange
        Optional<User> sameUser = Optional.of(follower);

        // Act & Assert
        SameFollowerAndFollowedException exception = assertThrows(
            SameFollowerAndFollowedException.class,
            () -> createFollowerRelationship.createRelationship(sameUser, sameUser)
        );

        assertEquals("SAME_FOLLOWER_AND_FOLLOWED", exception.getCode());
        assertEquals("User and the followed cannot be equal", exception.getMessage());
    }
}
