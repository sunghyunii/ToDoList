package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.Dto.DeleteTodoDto;
import com.sunghyun.todoapp.Dto.TodoRecommendationDto;
import com.sunghyun.todoapp.Dto.TodoRequestDto;

import com.sunghyun.todoapp.Dto.TodoResponseDto;
import com.sunghyun.todoapp.Entity.GeminiRecommendationDto;
import com.sunghyun.todoapp.service.RecommendService;
import com.sunghyun.todoapp.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final RecommendService recommendService;
    // 할 일 추가
    @PostMapping("/api/user/todo")
    public ResponseEntity<?> create(@RequestBody @Valid TodoRequestDto dto,
                                    @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        TodoResponseDto todo = todoService.createTodo(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }
    // 할 일 조회
    @GetMapping("/api/user/todo")
    public ResponseEntity<?> getTodoList(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        List<TodoResponseDto> todo = todoService.readTodo(userId, date);
        return ResponseEntity.ok(todo);
    }

    // 할 일 수정
    @PutMapping("/api/user/todo/{todoId}")
    public ResponseEntity<?> updateTodo(@RequestBody @Valid TodoRequestDto todoDto,
                                        @PathVariable Long todoId,
                                        @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        TodoResponseDto todo = todoService.updateTodo(todoId, todoDto, userId);
       return ResponseEntity.ok(todo);
    }
    // 할 일 삭제
    @DeleteMapping("/api/user/todo/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId,
                                        @AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        DeleteTodoDto dto = todoService.deleteTodo(todoId, userId);
        return ResponseEntity.ok(dto);
    }
    // gemini(할일) 기반으로 할일 추천
    @GetMapping("/api/user/todo/recommendGemini")
    public ResponseEntity<?> recommendGemini(@AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        String prompt = recommendService.makePrompt(userId);
        String todo = recommendService.getTodoRecommendation(prompt);
        return ResponseEntity.ok(new GeminiRecommendationDto(todo));
    }
    // 자주 하는 일 기반 할일 추천
    @GetMapping("/api/user/todo/recommend")
    public ResponseEntity<?> recommend(@AuthenticationPrincipal UserDetails userDetails){
        String userId = userDetails.getUsername();
        List<String> todo = recommendService.getTopTodoPatterns(userId);
        return ResponseEntity.ok(new TodoRecommendationDto(todo));
    }

}
