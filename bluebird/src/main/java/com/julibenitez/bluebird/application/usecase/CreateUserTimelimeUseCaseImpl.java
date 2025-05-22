package com.julibenitez.bluebird.application.usecase;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.julibenitez.bluebird.domain.exceptions.AuthorNotFoundException;
import com.julibenitez.bluebird.domain.exceptions.UserIdNotValidException;
import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;
import com.julibenitez.bluebird.domain.model.UserTimeline;
import com.julibenitez.bluebird.domain.ports.in.CreateUserTimelimeUseCase;
import com.julibenitez.bluebird.dtos.TweetRequestDto;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.FollowRepository;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserRepository;
import com.julibenitez.bluebird.infrastructure.persistence.repositories.UserTimelineRepository;

@Component
public class CreateUserTimelimeUseCaseImpl implements CreateUserTimelimeUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateUserTimelimeUseCaseImpl.class);
    private static final int BATCH_SIZE = 100;
    private static final String AUTHOR_NOT_FOUND = "Author with ID {} not found. Skipping tweet propagation.";
    private static final String AUTHOR_NOT_FOUND_CODE = "AUTHOR_NOT_FOUND";
    private static final String USER_ID_NOT_VALID = "User ID {} is not valid. Skipping tweet propagation.";
    private static final String USER_ID_NOT_VALID_CODE = "USER_ID_NOT_VALID";
    private final UserTimelineRepository userTimelineRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public CreateUserTimelimeUseCaseImpl(UserTimelineRepository userTimelineRepository,
            FollowRepository followRepository, UserRepository userRepository) {
        this.userTimelineRepository = userTimelineRepository;
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void execute(TweetRequestDto tweetRequestDto) {
        UUID authorId;
        try {
             authorId = UUID.fromString(tweetRequestDto.userId());
        } catch (IllegalArgumentException e) {
            String message = String.format(USER_ID_NOT_VALID, tweetRequestDto.userId());
            log.warn(message);
            throw new UserIdNotValidException(USER_ID_NOT_VALID_CODE, message);
        }
        User author = userRepository.findById(authorId).orElseGet(() -> {
                String message = String.format(AUTHOR_NOT_FOUND, authorId);
            log.warn(message);
            throw new AuthorNotFoundException(AUTHOR_NOT_FOUND_CODE, message);
        });

        List<Follow> followers = followRepository.findByFollower(author);

        if (followers.isEmpty()) {
            // followers don't exist
            log.info("No followers found for user {}. No timeline updates needed.", author.getId());
            return;
        }

        List<UserTimeline> timelineEntries = followers.stream()
                .map(follower -> new UserTimeline.Builder()
                        .authorId(author.getId())
                        .content(tweetRequestDto.content())
                        .createdAtDefault()
                        .followerId(follower.getId())
                        .tweetId(tweetRequestDto.id())
                        .newId()
                        .build())
                .toList();

        // save user-timelines
        batchSave(timelineEntries);
        log.info("Propagated tweet {} to {} followers",
                tweetRequestDto.id(), timelineEntries.size());
    }

    private void batchSave(List<UserTimeline> entries) {
        for (int i = 0; i < entries.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, entries.size());
            List<UserTimeline> batch = entries.subList(i, end);
            userTimelineRepository.saveAll(batch);
            userTimelineRepository.flush(); // fuerza persistencia inmediata
        }
    }

}
