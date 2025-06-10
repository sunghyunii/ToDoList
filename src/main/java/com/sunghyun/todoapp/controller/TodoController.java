package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.Dto.TodoDto;

import com.sunghyun.todoapp.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    @PostMapping("/api/user/todo")
    public ResponseEntity<?> create(@RequestBody @Valid TodoDto dto,
                                    @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        TodoDto todo = todoService.createTodo(userId, dto);
        return ResponseEntity.ok(todo);
    }
    @GetMapping("/api/user/todo")
    public ResponseEntity<?> getTodoList(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        List<TodoDto> todo = todoService.readTodo(userId, date);
        return ResponseEntity.ok(todo);
    }
}
