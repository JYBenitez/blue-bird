package com.julibenitez.bluebird.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user-timelines")
public class UserTimeline {
    @Id
    private UUID id;
    private UUID followerId;
    private UUID tweetId;
    private UUID authorId;
    private String content;
    private LocalDateTime createdAt;

    public UserTimeline() {
    }

    private UserTimeline(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.followerId = builder.followerId;
        this.tweetId = builder.tweetId;
        this.authorId = builder.authorId;
        this.content = builder.content;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public UUID getTweetId() {
        return tweetId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private UUID id;
        private UUID followerId;
        private UUID tweetId;
        private UUID authorId;
        private String content;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder newId() {
            this.id = UUID.randomUUID();
            return this;
        }

        public Builder followerId(UUID followerId) {
            this.followerId = followerId;
            return this;
        }

        public Builder tweetId(UUID tweetId) {
            this.tweetId = tweetId;
            return this;
        }

        public Builder authorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder createdAtDefault() {
            this.createdAt = LocalDateTime.now();
            return this;
        }

        public UserTimeline build() {
            return new UserTimeline(this);
        }
    }
}
