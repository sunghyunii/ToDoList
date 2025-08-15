package com.sunghyun.todoapp.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeminiRecommendationDto {
    private String todo;
    public GeminiRecommendationDto(String todo) {
        this.todo = todo;
    }
}
