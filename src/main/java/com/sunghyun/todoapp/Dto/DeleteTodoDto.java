package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DeleteTodoDto {
    private Long todoId;
    private String message;
    public DeleteTodoDto(Long todoId, String message){
        this.todoId = todoId;
        this.message = message;
    }
}
