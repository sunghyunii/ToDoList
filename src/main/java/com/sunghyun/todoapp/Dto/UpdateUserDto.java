package com.sunghyun.todoapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class UpdateUserDto {
    private String email;
    private String nickname;
    private String image;
    private String introduction;
}
