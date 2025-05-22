package com.julibenitez.bluebird.infrastructure.persistence.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.julibenitez.bluebird.domain.model.UserTimeline;

public interface UserTimelineRepository extends JpaRepository<UserTimeline, UUID> {
    @Query("""
            SELECT u FROM UserTimeline u
            WHERE u.followerId = :followerId
            AND (:lastSeenId IS NULL OR u.id < :lastSeenId)
            ORDER BY u.id DESC
            """)
    List<UserTimeline> findTimelineByFollowerIdWithCursor(
            @Param("followerId") UUID followerId,
            @Param("lastSeenId") UUID lastSeenId,
            Pageable pageable);
}
