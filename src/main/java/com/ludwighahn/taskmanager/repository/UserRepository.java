package com.ludwighahn.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ludwighahn.taskmanager.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
