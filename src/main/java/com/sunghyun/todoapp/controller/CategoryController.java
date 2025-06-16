package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.Dto.CategoryRequestDto;
import com.sunghyun.todoapp.Dto.CategoryResponseDto;
import com.sunghyun.todoapp.repository.CategoryRepository;
import com.sunghyun.todoapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 카테고리 추가
    @PostMapping("/api/user/category")
    public ResponseEntity<?> create(@RequestBody @Valid CategoryRequestDto categoryDto,
                                    @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        CategoryResponseDto responseDto = categoryService.createCategory(categoryDto, userId);
        return ResponseEntity.ok(responseDto);
    }
}
