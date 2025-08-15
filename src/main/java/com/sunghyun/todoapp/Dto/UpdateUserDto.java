package com.sunghyun.todoapp.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class UpdateUserDto {
    @NotBlank(message="email은 필수 값 입니다.")
    @Email
    private String email;
    @NotBlank(message="nickname은 필수 값 입니다.")
    private String nickname;
    private String image;
    private String introduction;
}
