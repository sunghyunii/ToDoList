package com.sunghyun.todoapp.controller;

import com.sunghyun.todoapp.security.filter.JwtAuthenticationProcessingFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/token")
public class TokenController {
    @PostMapping
    public void reissueToken(HttpServletRequest request, HttpServletResponse response){

    }
}
