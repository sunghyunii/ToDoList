package com.sunghyun.todoapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.todoapp.repository.UserRepository;
import com.sunghyun.todoapp.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sunghyun.todoapp.security.filter.JwtAuthenticationProcessingFilter;
import com.sunghyun.todoapp.security.model.LoginFailureHandler;
import com.sunghyun.todoapp.security.model.LoginSuccessJWTProvideHandler;
import com.sunghyun.todoapp.service.JwtService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration // 스프링 설정 클래스임을 선언
@EnableWebSecurity //웹 보안 기능 활성화
@RequiredArgsConstructor //lombok 어노테이션, final 필드 의존성 주입
public class SecurityConfig {
    private final UserDetailsService userDetailsService; // 사용자 정보 조회 서비스
    private final ObjectMapper objectMapper; // JSON 변환용(주로 JWT)
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors(cors -> cors.configurationSource(request ->{
            var corsConfig = new CorsConfiguration();
            corsConfig.addAllowedOrigin("*");
            corsConfig.addAllowedMethod("*");
            corsConfig.addAllowedHeader("*");
            corsConfig.addExposedHeader("Authorization");
            corsConfig.addExposedHeader("Authorization-refresh");
            return corsConfig;
        }));
        http    .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화(API 서비스에 적합)
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/user/login", "/api/user/register").permitAll()
                        .requestMatchers("/api/user").authenticated()
                        .anyRequest().permitAll() // 그 외 모든 요청 인증 필요
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 후 이동 페이지
                        .invalidateHttpSession(true) // 세션 무효화
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 사용 안 함(주로 JWT 사용시)
        http
                .addFilterAfter(jsonUsernamePasswordLoginFilter(), LogoutFilter.class) // jsonUsernamePasswordLoginFilter 를 logoutFilter 다음에 실행되도록 추가
                .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 데이터베이스 기반 인증 처리 담당
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception{
        // DaoAuthenticationProvider: 입력받은 비밀번호와 DB 에 저장된 암호화된 비밀번호 비교
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService); // 사용자 정보 조회 서비스 주입
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // 비밀번호 검증용 인코더 설정
        return daoAuthenticationProvider;
    }
    // 패스워드 인코더
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 다중 암호화 알고리즘 지원
    }
    // 인증 관리자 설정
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        DaoAuthenticationProvider provider = daoAuthenticationProvider(); //DB 기반 인증 로직 구현체
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler(){
        return new LoginSuccessJWTProvideHandler(jwtService, userRepository);
    }
    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }
    // JSON 인증 필터 등록
    // /login 경로의 JSON 요청 처리
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter() throws Exception{
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        jsonUsernamePasswordLoginFilter.setFilterProcessesUrl("/api/user/login");
        return jsonUsernamePasswordLoginFilter;
    }
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jsonUsernamePasswordLoginFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);

        return jsonUsernamePasswordLoginFilter;
    }
}
