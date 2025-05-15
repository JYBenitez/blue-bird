package com.julibenitez.bluebird.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tweet {
 private String id;
    private String userId;
    private String content;
    private LocalDateTime createdAt;

    private Tweet(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.userId = builder.userId;
        this.content = builder.content;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
   
    }
    public String getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }
    public String getContent() {
        return content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private String id;
        private String userId;
        private String content;
        private LocalDateTime createdAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder newId() {
            this.id = UUID.randomUUID().toString();
            return this;
        }


        public Builder userId(String userId) {
            this.userId = userId;
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

        public Tweet build() {
            return new Tweet(this);
        }
    }
}