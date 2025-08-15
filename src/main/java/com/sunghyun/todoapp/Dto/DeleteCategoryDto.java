package com.sunghyun.todoapp.Dto;

import lombok.Getter;

@Getter
public class DeleteCategoryDto {
    private Long categoryId;
    private String message;

    public DeleteCategoryDto(Long categoryId, String message) {
        this.categoryId = categoryId;
        this.message = message;
    }
}
