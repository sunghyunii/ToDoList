package com.sunghyun.todoapp.security.filter;

import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.UserRepository;
import com.sunghyun.todoapp.security.model.UserDetailsImpl;
import com.sunghyun.todoapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


// 사용자 요청이 들어올 때마다 토큰을 확인하고 인증 처리를 담당한다
// 로그인 요청은 처리하지 않고 통과시킨다
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final List<String> NO_CHECK_URL = List.of("/api/login","/api/register");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("jwtAuthenticationProcessingFilter 실행됨");
        // / 1. login 경로로 오는 요청은 이 필터를 건너뛰고 다음 필터로
        if(NO_CHECK_URL.contains(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }
        Optional<String> refreshTokenOpt = jwtService.extractRefreshToken(request);
        refreshTokenOpt.ifPresentOrElse(
                token -> log.debug("추출된 refreshToken: {}", token),
                () -> log.warn("refreshToken 헤더에서 추출 실패")
        );

        // 모든 요청에서 refresh token 을 찾는 이유
        // 토큰 갱신 로직을 간소화, 서버가 자동으로 토큰 만료를 처리
        // 2. refresh Token 추출 후 유효한지 검사
        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        // 2-1. refresh Token 있으면 Access Token 발급
        if(refreshToken != null && isReissueRequest(request)){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken, request, filterChain);
            return;
        }
        // 3. RefreshToken 없거나 유효하지 않으면 AccessToken 확인
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }
    // Access Token을 추출하고 유효할 때 spring security에 인증 정보 저장
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.extractAccessToken(request).orElse(null);

        // 토큰이 유효하면 사용자 id 추출해서 인증 객체(SecurityContext) 에 저장
        if (token != null && jwtService.isTokenValid(token)) {
            jwtService.extractId(token).ifPresent(id ->
                    userRepository.findById(id).ifPresent(this::saveAuthentication)
            );
        }
        // 인증을 넣었든 아니든, 필터 체인은 마지막에 호출
        filterChain.doFilter(request, response);
    }
    private void saveAuthentication(User user){
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
        // 인증 객체 생성
        // 현재 인증된 사용자 정보를 관리하는 SecutityContextHolder 를 통해 사용자 정보 관리하고
        // 인증 정보를 저장함으로써 애플리케이션 전체에서 인증된 사용자의 id 와 권한에 접근할 수 있다
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        System.out.println("== 사용자 인증 저장: " + user.getEmail());
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken,  HttpServletRequest request, FilterChain filterChain) {

        userRepository.findByRefreshToken(refreshToken).ifPresentOrElse(
                user -> {
                    String newAccessToken = jwtService.createAccessToken(user.getId());
                    jwtService.sendAccessToken(response, newAccessToken);
                    saveAuthentication(user);
                },
                () -> log.warn("유효하지 않은 refreshToken 요청")
        );

    }
    private boolean isReissueRequest(HttpServletRequest request){
        return "/api/user/token".equals(request.getRequestURI());
    }
}
