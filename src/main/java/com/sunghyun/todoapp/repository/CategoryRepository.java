package com.sunghyun.todoapp.repository;

import com.sunghyun.todoapp.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
