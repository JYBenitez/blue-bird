package com.julibenitez.bluebird.domain.ports.in;

public interface CreateFollowerRelationshipUseCase {
    void execute(String follower, String followed);
}
