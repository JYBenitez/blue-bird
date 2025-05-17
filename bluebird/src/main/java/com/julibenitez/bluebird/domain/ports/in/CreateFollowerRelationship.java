package com.julibenitez.bluebird.domain.ports.in;

import java.util.Optional;

import com.julibenitez.bluebird.domain.model.Follow;
import com.julibenitez.bluebird.domain.model.User;

public interface CreateFollowerRelationship {
    Follow createRelationship(Optional<User> user, Optional<User> follow);
}
