package com.sunghyun.todoapp.security.model;

import com.sunghyun.todoapp.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private final User user;
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    // 사용자에게 부여된 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // GrantedAuthority: 사용자의 약힐을 나타내는 인터페이스 GrantedAuthority 로 변환하여 반환
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId();
    }
    // 계정이 만료되었는지 여부 (true 면 만료되지 않았으며 로그인 가능)

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 사용자의 계정이 잠겨있는지 여부 (특정 조건(비밀번호 여러 번 틀렸을 때) 으로 인해 계정을 잠굴 수 있다)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 사용자의 인증 자격 증명(비밀번호)이 만료되었는지 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용자의 계정이 활성화되었는지 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
}
