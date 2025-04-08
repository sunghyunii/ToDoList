package com.sunghyun.todoapp.repository;

import com.sunghyun.todoapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
}
