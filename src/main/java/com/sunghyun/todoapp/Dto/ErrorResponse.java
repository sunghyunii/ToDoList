package com.sunghyun.todoapp.Dto;

import java.util.List;

public class ErrorResponse {
    private String message;
    public ErrorResponse(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
