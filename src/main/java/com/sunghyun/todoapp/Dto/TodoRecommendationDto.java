package com.sunghyun.todoapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class TodoRecommendationDto {
    private String recommendation;
    public TodoRecommendationDto(String recommendation){
        this.recommendation = recommendation;
    }
}
