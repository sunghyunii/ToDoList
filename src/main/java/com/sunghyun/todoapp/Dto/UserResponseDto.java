package com.sunghyun.todoapp.Dto;

import com.sunghyun.todoapp.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String nickname;
    private String image;
    private String introduction;

    public UserResponseDto(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.introduction = user.getIntroduction();
    }
}
