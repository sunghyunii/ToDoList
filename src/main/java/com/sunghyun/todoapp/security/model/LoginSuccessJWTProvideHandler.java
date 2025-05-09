package com.sunghyun.todoapp.security.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.todoapp.repository.UserRepository;
import com.sunghyun.todoapp.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

/** 성공를 처리할 handler */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String id = extractId(authentication);
        String accessToken = jwtService.createAccessToken(id);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        userRepository.findById(id).ifPresent(
                user -> user.updateRefreshToken(refreshToken)
        );

        System.out.println("로그인 성공, JWT 발급. username: " + id);
        System.out.println("AccessToken 발급. AccessToken: " + accessToken);
        System.out.println("RefreshToken 발급. RefreshToken: " + refreshToken);
        // 헤더에 토큰 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Authorization-refresh", "Bearer " + refreshToken);
        // body 에 추가
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        response.setContentType("application/json");
        //String jsonResponse = String.format(
        //        "{\"success\":true,\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}",
        //        accessToken, refreshToken
        //);

        //response.getWriter().write("jsonResponse");
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
    }

    private String extractId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
