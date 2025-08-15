package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TodoResponseDto {
    private Long todoId;
    private String content;
    private Status status;
    private LocalDate date;
    private Category category;
    public TodoResponseDto(Todo todo){
        this.todoId = todo.getId();
        this.content = todo.getContent();
        this.status = todo.getStatus();
        this.date = todo.getDate();
        this.category = todo.getCategory();
    }
}
