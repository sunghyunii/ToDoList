package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.Role;
import lombok.Data;

@Data
public class CreateUserDto {
    private String id;
    private String password;
    private String email;
    private String nickname;
    private String image;
    private String introduction;
}
