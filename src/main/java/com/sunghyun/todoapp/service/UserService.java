package com.sunghyun.todoapp.service;

import com.sunghyun.todoapp.Dto.CreateUserDto;
import com.sunghyun.todoapp.Entity.User;
import com.sunghyun.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //회원가입
    @Transactional
    public User join(CreateUserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());
        user.setIntroduction(userDto.getIntroduction());
        validateDuplicateMember(user);
        userRepository.save(user);
        return user;
    }

    //
    private void validateDuplicateMember(User user){
        if(user.getId() == null){
            throw new IllegalStateException("id 값이 null 입니다");
        }
        Optional<User> findUser =  userRepository.findById(user.getId());
        if(findUser.isPresent()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

}
