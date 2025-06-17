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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    // 카테고리 조회
    @GetMapping("/api/user/category")
    public ResponseEntity<?> getCategorise(@AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        List<CategoryResponseDto> categoryList = categoryService.readCategory(userId);
        return ResponseEntity.ok(categoryList);
    }
    //카테고리 수정
    @PutMapping("/api/user/category/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CategoryRequestDto categoryDto,
                                            @PathVariable Long categoryId,
                                            @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        CategoryResponseDto responseDto = categoryService.updateCategory(categoryDto, categoryId, userId);
        return ResponseEntity.ok(responseDto);
    }
}
