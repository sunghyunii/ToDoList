package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.CreateUserDto;
import com.sunghyun.todoapp.Dto.UpdateUserDto;
import com.sunghyun.todoapp.Dto.UserResponseDto;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 회원가입 */
    @Transactional
    public UserResponseDto join(CreateUserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());
        user.setIntroduction(userDto.getIntroduction());

        validateRequiredFields(user);
        validateUser(user);

        userRepository.save(user);
        return new UserResponseDto(user);
    }
    private void validateRequiredFields(User user){
        if(user.getId() == null || user.getId().isEmpty()){
            throw new IllegalArgumentException("id 는 필수 값입니다. ");
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            throw new IllegalArgumentException("password 는 필수 값입니다. ");
        }
        if(user.getEmail() == null || user.getEmail().isEmpty()){
            throw new IllegalArgumentException("email 은 필수 값입니다. ");
        }
        if(user.getNickname() == null || user.getNickname().isEmpty()){
            throw new IllegalArgumentException("nickname 은 필수 값입니다. ");
        }
    }
    private void validateUser(User user){
        validateId(user.getId());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateNickname(user.getNickname());
    }
    // ID 검증
    private void validateId(String id){
        if(userRepository.findById(id).isPresent()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    // 이메일 중복 확인
    private void validateEmail(String email){
        if(userRepository.existsByEmail(email)){
            throw new IllegalStateException("이미 존재하는 이메일입니다");
        }
    }
    private void validateNickname(String nickname){
        if(userRepository.existsByNickname(nickname)){
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
    }

    // 비밀번호 유효성 검사
    private void validatePassword(String password){
        if(password.length() < 8){
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
        }
    }
    @Transactional
    public void updateRefreshToken(String userId, String refreshToken){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
    /**회원정보 조회*/
    public UserResponseDto getUserInfo(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new UserResponseDto(user);
    }

    /**회원정보 수정*/
    @Transactional
    public UserResponseDto updateUserInfo(String userId, UpdateUserDto userDto) {
        if(userDto.getEmail() == null || userDto.getEmail().isEmpty()){
            throw new IllegalArgumentException("email 은 필수 값입니다. ");
        }
        if(userDto.getNickname() == null || userDto.getNickname().isEmpty()){
            throw new IllegalArgumentException("nickname 은 필수 값입니다. ");
        }
        // 이메일 중복 확인
        if(userRepository.existsByEmailAndIdNot(userDto.getEmail(), userId)){
                throw new IllegalStateException("이미 존재하는 이메일입니다");
        }
        // 닉네임 중복 확인
        if(userRepository.existsByNicknameAndIdNot(userDto.getNickname(), userId)){
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
        // 사용자 정보 업데이트
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());
        user.setIntroduction(userDto.getIntroduction());
        return new UserResponseDto(user);
    }

}
