package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequestDto {
    @NotBlank(message="내용은 필수 값입니다.")
    private String content;
    private Status status;
    private LocalDate date;
    private Category category;
}

