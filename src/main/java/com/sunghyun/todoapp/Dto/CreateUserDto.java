package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotBlank(message = "id는 필수 값 입니다.")
    private String id;
    @NotBlank(message = "password는 필수 값 입니다.")
    @Size(min=8,message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    @NotBlank(message = "email은 필수 값 입니다.")
    @Email
    private String email;
    @NotBlank(message = "nickname은 필수 값 입니다.")
    private String nickname;
    private String image;
    private String introduction;
}
