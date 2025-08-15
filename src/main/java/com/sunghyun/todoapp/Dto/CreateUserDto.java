package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotBlank(message = "id는 필수 값 입니다.")
    @Schema(description = "사용자 id", nullable = false, example = "testId")
    private String id;
    @NotBlank(message = "password는 필수 값 입니다.")
    @Size(min=8,message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Schema(description = "사용자 비밀번호", nullable = false, example = "12345678")
    private String password;
    @NotBlank(message = "email은 필수 값 입니다.")
    @Email
    @Schema(description = "사용자 이메일", nullable = false, example = "test@gmail.com")
    private String email;
    @NotBlank(message = "nickname은 필수 값 입니다.")
    @Schema(description = "사용자 닉네임", nullable = false, example = "test user")
    private String nickname;
    @Schema(description = "사용자 프로필 사진", nullable = true, example = "image.jpg")
    private String image;
    @Schema(description = "사용자 자기소개", nullable = true, example = "안녕하세요")
    private String introduction;
}
