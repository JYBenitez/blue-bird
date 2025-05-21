package com.julibenitez.bluebird.infrastructure.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    List<Follow> findByFollower(User follower);
}
