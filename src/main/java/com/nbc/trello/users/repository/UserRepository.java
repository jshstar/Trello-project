package com.nbc.trello.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.nbc.trello.users.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
