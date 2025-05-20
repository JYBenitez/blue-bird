package com.julibenitez.bluebird.infrastructure.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julibenitez.bluebird.domain.ports.in.CreateFollowerRelationshipUseCase;
import com.julibenitez.bluebird.dtos.FollowRequestDto;

@RestController
@RequestMapping("api/v1/follower")
public class FollowController {
    private final CreateFollowerRelationshipUseCase createFollowerRelationshipUseCase;

    public FollowController(CreateFollowerRelationshipUseCase createFollowerRelationshipUseCase) {
        this.createFollowerRelationshipUseCase = createFollowerRelationshipUseCase;
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<Void> follow(
            @PathVariable("user_id") String userId,
            @RequestBody FollowRequestDto request) {
        createFollowerRelationshipUseCase.execute(userId, request.followed());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
