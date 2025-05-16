package com.julibenitez.bluebird.infrastructure.persistence.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.julibenitez.bluebird.domain.model.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, UUID> {
    List<Tweet> findByUserId(UUID userId);

}
