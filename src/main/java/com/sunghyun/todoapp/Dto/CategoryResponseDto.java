package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(Category category){
        id = category.getId();
        name = category.getName();
    }
}
