package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.CategoryRequestDto;
import com.sunghyun.todoapp.Dto.DeleteCategoryDto;
import com.sunghyun.todoapp.Dto.CategoryResponseDto;
import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.CategoryRepository;
import com.sunghyun.todoapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto, String userId) {
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

    public List<CategoryResponseDto> readCategory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return categoryRepository.findByUser(user).stream().
                map(CategoryResponseDto::new).
                collect(Collectors.toList());
    }
    @Transactional
    public CategoryResponseDto updateCategory(CategoryRequestDto categoryDto, Long categoryId, String userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));
        if(!category.isOwnedBy(userId)){
            throw new AccessDeniedException("수정 권한이 없습니다");
        }
        String name = categoryDto.getName();
        category.setName(name.trim());

        return new CategoryResponseDto(category);
    }

    public DeleteCategoryDto deleteCategory(Long categoryId, String userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다."));
        if(!category.isOwnedBy(userId)){
            throw new AccessDeniedException("삭제 권한이 없습니다");
        }
        categoryRepository.deleteById(categoryId);
        return new DeleteCategoryDto(categoryId, "할 일이 삭제되었습니다.");

    }
}
