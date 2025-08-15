package com.sunghyun.todoapp.repository;

import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByDate(LocalDate date);

    List<Todo> findByUserIdAndDate(String userId, LocalDate date);

    @Query("SELECT t.content, COUNT(t) FROM Todo t WHERE t.user.id = :userId GROUP BY t.content ORDER BY COUNT(t) DESC")
    List<Object[]> findTopFrequentContentsByUser(@Param("userId") String userId);
    List<Todo> findByUserIdAndStatusAndDate(String userId, Status status, LocalDate today);
}
