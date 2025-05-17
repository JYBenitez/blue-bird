package com.julibenitez.bluebird.infrastructure.persistence.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.julibenitez.bluebird.domain.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
