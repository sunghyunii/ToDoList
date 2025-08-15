package com.sunghyun.todoapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.todoapp.Dto.ErrorResponse;
import com.sunghyun.todoapp.Dto.UpdateUserDto;
import com.sunghyun.todoapp.Dto.UserResponseDto;
import com.sunghyun.todoapp.Entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse error = new ErrorResponse("인증 정보가 유효하지 않습니다.");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
