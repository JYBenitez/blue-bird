package com.julibenitez.bluebird.application.usecase;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;
import com.julibenitez.bluebird.domain.ports.in.CreateFollowerRelationship;
import com.julibenitez.bluebird.domain.ports.in.CreateFollowerRelationshipUseCase;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.FollowRepository;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserRepository;

@Component
public class CreateFollowerRelationshipUseCaseImpl implements CreateFollowerRelationshipUseCase {
    private final UserRepository userRepository;
    private final CreateFollowerRelationship createFollowerRelationship;
    private final FollowRepository followRepository;

    public CreateFollowerRelationshipUseCaseImpl(UserRepository userRepository,
            CreateFollowerRelationship createFollowerRelationship,
            FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.createFollowerRelationship = createFollowerRelationship;
        this.followRepository = followRepository;
    }

    @Override
    public void execute(String follower, String followed) {
        Optional<User> userFollower = userRepository.findById(UUID.fromString(follower));
        Optional<User> userFollowed = userRepository.findById(UUID.fromString(followed));

        Follow follow = createFollowerRelationship.createRelationship(userFollower, userFollowed);
        followRepository.save(follow);
    }

}
