package com.sunghyun.todoapp.repository;

import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndUser(String name, User user);

    List<Category> findByUser(User user);

    Category findByIdAndUser(Long categoryId, User user);
}
