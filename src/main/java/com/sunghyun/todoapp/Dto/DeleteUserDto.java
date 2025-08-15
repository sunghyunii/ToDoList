package com.sunghyun.todoapp.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteUserDto {
    private String message;
    public DeleteUserDto(String message){
        this.message = message;
    }
}
