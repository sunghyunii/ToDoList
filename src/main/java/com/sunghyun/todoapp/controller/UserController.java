package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.Dto.CreateUserDto;
import com.sunghyun.todoapp.Dto.LoginDto;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/api/user/register")
    public String createForm(){
        return "";
    }
    @PostMapping("/api/user/register")
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserDto userDto){
        if(userDto.getId() == null){
            return ResponseEntity.badRequest().body("Id 값이 필요합니다");
        }

        userService.join(userDto);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/api/user/login")
    public void login(@RequestBody @Valid LoginDto loginDto){

    }

    // 사용자 정보 조회
    @GetMapping("/api/user/{id}")
    public ResponseEntity<?> read(){

    }
}
