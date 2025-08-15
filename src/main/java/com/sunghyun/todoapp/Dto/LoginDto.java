package com.sunghyun.todoapp.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    @NotNull
    private String id;
    @NotNull
    private String password;
}
