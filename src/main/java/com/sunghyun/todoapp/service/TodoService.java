package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.DeleteTodoDto;
import com.sunghyun.todoapp.Dto.TodoRequestDto;
import com.sunghyun.todoapp.Dto.TodoResponseDto;
import com.sunghyun.todoapp.Entity.Category;
import com.sunghyun.todoapp.Entity.Status;
import com.sunghyun.todoapp.Entity.Todo;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.CategoryRepository;
import com.sunghyun.todoapp.repository.TodoRepository;
import com.sunghyun.todoapp.repository.UserRepository;
import jakarta.persistence.Access;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public TodoResponseDto createTodo(String userId, TodoRequestDto dto) {
        Todo todo = new Todo();
        todo.setContent(dto.getContent());
        if(dto.getStatus() == null){
            dto.setStatus(Status.TODO);
        }
        todo.setStatus(dto.getStatus());
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
        return new TodoResponseDto(todo);
    }

    // 할 일 조회
    public List<TodoResponseDto> readTodo(String userId, LocalDate date) {
        return todoRepository.findByUserIdAndDate(userId, date)
                .stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 할 일 수정
    @Transactional
    public TodoResponseDto updateTodo(Long todoId, TodoRequestDto dto, String userId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException("해당 할 일이 존재하지 않습니다"));
        if(!todo.isOwnedBy(userId)){
            throw new AccessDeniedException("수정 권한이 없습니다");
        }
        todo.setContent(dto.getContent());
        todo.setStatus(dto.getStatus());
        todo.setDate(dto.getDate());
        if(dto.getCategory() != null){
            Category category = categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
            todo.setCategory(category);
        } else{
            todo.setCategory(null);
        }
        return new TodoResponseDto(todo);
    }
    //
    @Transactional
    public DeleteTodoDto deleteTodo(Long todoId, String userId) throws AccessDeniedException {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()->new EntityNotFoundException("해당 할 일이 존재하지 않습니다."));
        if(!todo.isOwnedBy(userId)){
            throw new AccessDeniedException("삭제 권한이 없습니다");
        }
        todoRepository.deleteById(todoId);
        return new DeleteTodoDto(todoId, "할 일이 삭제되었습니다");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Todo> getUnfinishedTodosDueToday(String userId) {
        Status status = Status.TODO;
        LocalDate today = LocalDate.now();
        return todoRepository.findByUserIdAndStatusAndDate(userId, status,today);
    }
}
