package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.TodoDto;
import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.CategoryRepository;
import com.sunghyun.todoapp.repository.TodoRepository;
import com.sunghyun.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

   // 할 일 추가
    @Transactional
    public TodoDto createTodo(String userId, TodoDto dto) {
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수 값입니다");
        }

        Todo todo = new Todo();
        todo.setContent(dto.getContent());
        todo.setStatus(Status.TODO);
        todo.setDate(dto.getDate());
        if(dto.getCategory() != null){
            Category category = categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
            todo.setCategory(category);
        } else{
            todo.setCategory(null);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        todo.setUser(user);
        todoRepository.save(todo);
        return new TodoDto(todo);
    }

    // 할 일 조회
    public List<TodoDto> readTodo(String userId, LocalDate date) {
        List<Todo> todoList = todoRepository.findByDate(date);

        return todoList.stream()
                .map(TodoDto::new)
                .collect(Collectors.toList());
    }
}
