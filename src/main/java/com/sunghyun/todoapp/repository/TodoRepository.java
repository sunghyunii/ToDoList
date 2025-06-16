package com.sunghyun.todoapp.repository;

import com.sunghyun.todoapp.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByDate(LocalDate date);

    List<Todo> findByUserIdAndDate(String userId, LocalDate date);
}
