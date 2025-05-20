package com.sunghyun.todoapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.todoapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtServiceImpl implements JwtService {
    /** jwt.yml 에 설정된 값 가져오기 */
    @Value("${jwt.secret}")
    private String secret; //  JWT 서명에 사용할 비밀 키
    @Value("${jwt.access.expiration}")
    private long accessTokenValidityInSeconds; // Access Token의 만료 시간
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds; // : Refresh Token의 만료 시간
    @Value("${jwt.access.header}")
    private String accessHeader; // Access Token이 저장될 HTTP 헤더 이름
    @Value("${jwt.refresh.header}")
    private String refreshHeader;
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "id";
    private static final String BEARER = "Bearer "; // 토큰 앞에 붙는 "Bearer " 문자열

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // JWT 생성 매서드
    @Override
    public String createAccessToken(String id) {
        System.out.println("=== createAccessToken 호출 ===");
        log.info("== createAccessToken() 사용 중인 secret: {}", secret);
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(secret));
    }


    @Override
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000))
                .sign(Algorithm.HMAC512(secret));
    }

    // Refresh Token 관리
    @Override
    public void updateRefreshToken(String id, String refreshToken) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("회원 조회 실패")
                );
    }
    // HTTP 응답에 토큰 추가
    // Access Token 과 Refresh Token http 응답 헤더에 추가
    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        Map<String, String> tokenMap = new HashMap<>();
    }

    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        response.setContentType("application/json");
        try{
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
            objectMapper.writeValue(response.getWriter(), tokenMap);

        }catch (IOException e){
            log.error("토큰 응답 전송 실패", e);
        }

    }

    // 요청에서 토큰 추출
    // 클라이언트의 신원을 증명하기 위해 보낸 access token 추출
    // "Bearer" 은 표준 http 인증 스키마로 클라이언트(프론트엔드)에서 HTTP 요청을 보낼 때 토큰 앞에 "Bearer " 접두사를 추가
    // Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println("== 클라이언트가 보낸 Authorization-refresh: " + header);

        return Optional.ofNullable(request.getHeader(accessHeader)).filter(
                accessToken -> accessToken.startsWith(BEARER)
        ).map(accessToken -> accessToken.replace(BEARER, "")); // "Bearer" 접두사 제거
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization-Refresh");
        System.out.println("Authorization-Refresh 헤더 값: " + header);
        return Optional.ofNullable(request.getHeader(header)).filter(
                refreshToken -> refreshToken.startsWith(BEARER)
        ).map(refreshToken -> refreshToken.replace(BEARER, "")); // "Bearer" 접두사 제거
    }


    // 토큰 검증 및 아이디 추출
    @Override
    public Optional<String> extractId(String accessToken) {
        // JWT.require(Algorithm.HMAC512(secret)) 이 비밀 키와 HMAC512 알고리즘으로 서명된 토큰만 유효
        // .build() require() 메서드로 정의한 구성을 바탕으로 실제 검증기 만듬
        // .verify(accessToken) jwt 유효성 검증(서명이 유효한지, 토큰 만료됐는지, 클레임 조건 충족 확인)
        // .getClaim(USERNAME_CLAIM).asString()) 토큰 내부 페이로드에 지정된 키에 해당하는 값을 찾아 문자열로 변환
        try{
            log.info("== extractId() 사용 중인 secret: {}", secret);
            return Optional.ofNullable(
                JWT.require(Algorithm.HMAC512(secret)).build().verify(accessToken)
                        .getClaim(USERNAME_CLAIM).asString());
        } catch(Exception e){
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    @Override
    public boolean isTokenValid(String token) {
        try{
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
    }catch (Exception e){
            log.error("유효하지 않은 token 입니다:{}", e.getMessage());
            return false;
        }
    }
}

