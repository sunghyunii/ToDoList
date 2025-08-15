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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 1. 사용자 ID 추출
        String id = extractId(authentication);

        // 2. AccessToken / RefreshToken 생성
        String accessToken = jwtService.createAccessToken(id);
        String refreshToken = jwtService.createRefreshToken();

        // 3. 토큰 클라이언트에 전송
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // 4. DB 에 RefreshToken 저장
        userRepository.findById(id).ifPresent(
                user -> user.updateRefreshToken(refreshToken)
        );

        System.out.println("로그인 성공, JWT 발급. username: " + id);
        System.out.println("AccessToken 발급. AccessToken: " + accessToken);
        System.out.println("RefreshToken 발급. RefreshToken: " + refreshToken);

        // 5. 응답 헤더 및 바디에 토큰 추가
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Authorization-refresh", "Bearer " + refreshToken);
        // JSON 형태로 body 에 토큰을 담아서 응답
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
    }

    private String extractId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
