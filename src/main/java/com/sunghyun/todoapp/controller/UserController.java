package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.Dto.*;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/api/register")
    public String createForm(){
        return "";
    }
    @PostMapping("/api/register")
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserDto userDto){
        userService.join(userDto);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/api/login")
    public void login(@RequestBody @Valid LoginDto loginDto){

    }
    // 사용자 정보 조회
    @GetMapping("/api/user")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null){
            System.out.println(">> userDetails is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보 없음");
        }
        String userId = userDetails.getUsername();
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 사용자 정보 수정
    @PutMapping("/api/user/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserDto userDto,
                                            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        if(userDetails == null){
            throw new AccessDeniedException("로그인이 필요합니다");
        }
        String userId = userDetails.getUsername();
        UserResponseDto dto = userService.updateUserInfo(userId,userDto);
        return ResponseEntity.ok(dto);
    }

    // 사용자 정보 삭제
    @DeleteMapping("/api/user/delete")
    public ResponseEntity<?> deleteUserInfo(@AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        if(userDetails == null){
            throw new AccessDeniedException("로그인이 필요합니다");
        }
        String userId = userDetails.getUsername();
        DeleteUserDto userInfo = userService.deleteUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }
}
