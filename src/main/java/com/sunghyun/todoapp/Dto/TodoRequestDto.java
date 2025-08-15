package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequestDto {
    @NotBlank(message="내용은 필수 값입니다.")
    private String content;
    private Status status;
    private LocalDate date;
    private Category category;
}

