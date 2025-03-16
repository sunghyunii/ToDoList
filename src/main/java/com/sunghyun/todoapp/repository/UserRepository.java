package com.sunghyun.todoapp.repository;

import com.sunghyun.todoapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
