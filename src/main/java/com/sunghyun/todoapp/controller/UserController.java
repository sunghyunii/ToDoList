package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.Dto.*;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "User API 입니다")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @Operation(summary = "회원가입", description = "사용자가 회원가입 화면 조회")
    @GetMapping("/api/register")
    public String createForm(){
        return "";
    }
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schemaProperties = {
                                    @SchemaProperty(name = "result", schema = @Schema(example = "SUCCESS")),
                                    @SchemaProperty(name = "data", schema = @Schema(implementation = UserResponseDto.class))
                            }
                    ))
    })
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 위해 정보 입력")
    @PostMapping("/api/register")
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserDto userDto){
        UserResponseDto dto = userService.join(userDto);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/api/login")
    public void login(@RequestBody @Valid LoginDto loginDto){

    }
    // 사용자 정보 조회
    @Operation(summary = "사용자 정보 조회", description = "사용자의 정보 조회")
    @GetMapping("/api/user")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails){
        log.info(">>> /api/user reached");
        if(userDetails == null){
            System.out.println(">> userDetails is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보 없음");
        }
        String userId = userDetails.getUsername();
        UserResponseDto userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }

    // 사용자 정보 수정
    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 수정")
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
    @Operation(summary = "사용자 정보 삭제", description = "사용자의 정보를 삭제")
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
