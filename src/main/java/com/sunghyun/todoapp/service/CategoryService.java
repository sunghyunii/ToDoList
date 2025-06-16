package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.CategoryRequestDto;
import com.sunghyun.todoapp.Dto.CategoryResponseDto;
import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.CategoryRepository;
import com.sunghyun.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    public CategoryResponseDto createCategory(CategoryRequestDto dto, String userId) {
        if(dto.getName() == null || dto.getName().trim().isEmpty()){
            throw new IllegalArgumentException("카테고리 이름을 입력하세요.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        boolean exist = categoryRepository.existsByNameAndUser(dto.getName(),user);
        if(exist){
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setUser(user);
        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }
}
