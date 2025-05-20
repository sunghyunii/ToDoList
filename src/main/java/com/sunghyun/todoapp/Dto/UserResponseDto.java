package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String email;
    private String nickname;
    private String image;
    private String introduction;
}
