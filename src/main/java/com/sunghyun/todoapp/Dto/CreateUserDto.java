package com.sunghyun.todoapp.Dto;

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
