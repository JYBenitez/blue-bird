package com.julibenitez.bluebird.domain.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.julibenitez.bluebird.domain.exceptions.FollowerOrFollowedEmptyException;
import com.julibenitez.bluebird.domain.exceptions.SameFollowerAndFollowedException;
import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;
import com.julibenitez.bluebird.domain.ports.in.CreateFollowerRelationship;

@Service
public class CreateFollowerRelationshipImpl implements CreateFollowerRelationship {

    private final String SAME_FOLLOWER_AND_FOLLOWED = "User and the followed cannot be equal";
    private final String SAME_FOLLOWER_AND_FOLLOWED_CODE = "SAME_FOLLOWER_AND_FOLLOWED";

    private final String FOLLOWER_OR_FOLLOWED_EMPTY = "Follower or followed couldn't empty";
    private final String FOLLOWER_OR_FOLLOWED_EMPTY_CODE = "FOLLOWER_OR_FOLLOWED_EMPTY";

    @Override
    public Follow createRelationship(Optional<User> user, Optional<User> followed) {
        validate(user, followed);
        return build(user.get(), followed.get());
    }

    private void validate(Optional<User> user, Optional<User> followed) {
        if (user.isEmpty() || followed.isEmpty())
            throw new FollowerOrFollowedEmptyException(FOLLOWER_OR_FOLLOWED_EMPTY_CODE,
                    FOLLOWER_OR_FOLLOWED_EMPTY);

        if (user.equals(followed))
            throw new SameFollowerAndFollowedException(SAME_FOLLOWER_AND_FOLLOWED_CODE,
                    SAME_FOLLOWER_AND_FOLLOWED);
    }

    private Follow build(User user, User followed) {
        return new Follow.Builder()
                .newId()
                .follower(user)
                .followed(followed)
                .createdAtDefault()
                .build();
    }

}
