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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// 사용자 요청이 들어올 때마다 토큰을 확인하고 인증 처리를 담당한다
// 로그인 요청은 처리하지 않고 통과시킨다
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final String NO_CHECK_URL = "/login";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // /login 경로로 오는 요청은 이 필터를 건너뛰고 다음 필터로
        if(request.getRequestURI().equals(NO_CHECK_URL)){
            filterChain.doFilter(request, response);
            return;
        }

        // 모든 요청에서 refresh token 을 찾는 이유
        // 토큰 갱신 로직을 간소화, 서버가 자동으로 토큰 만료를 처리
        // refresh Token 추출 후 유효한지 검사
        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        // refresh Token 있으면 Access Token 발급
        if(refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }
    // Access Token을 추출하고 유효할 때 spring security에 인증 정보 저장
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(
                accessToken -> jwtService.extractId(accessToken).ifPresent(
                        id -> userRepository.findById(id).ifPresent(
                                user -> saveAuthentication(user)
                        )
                )
        );
        filterChain.doFilter(request,response);

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
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken).ifPresent(
                user -> jwtService.sendAccessToken(response, jwtService.createAccessToken(user.getId()))
        );

    }
}
