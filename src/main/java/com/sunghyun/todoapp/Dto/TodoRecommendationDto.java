package com.sunghyun.todoapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TodoRecommendationDto {
    private List<String> recommendation = new ArrayList<>();
    public TodoRecommendationDto(List<String> recommendation){
        this.recommendation = recommendation;
    }
}
