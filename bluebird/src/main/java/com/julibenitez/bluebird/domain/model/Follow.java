package com.julibenitez.bluebird.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    private LocalDateTime createdAt;

    public Follow() {
    }

    private Follow(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.follower = builder.follower;
        this.followed = builder.followed;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowed() {
        return followed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private UUID id;
        private User follower;
        private User followed;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder newId() {
            this.id = UUID.randomUUID();
            return this;
        }

        public Builder follower(User follower) {
            this.follower = follower;
            return this;
        }

        public Builder followed(User followed) {
            this.followed = followed;
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

        public Follow build() {
            return new Follow(this);
        }
    }
}
