package com.julibenitez.bluebird.application.usecase;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        UUID authorId = UUID.fromString(tweetRequestDto.userId());
        User author = userRepository.findById(authorId).orElseGet(() -> {
            log.warn("Author with ID {} not found. Skipping tweet propagation.", authorId);
            return null;
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
