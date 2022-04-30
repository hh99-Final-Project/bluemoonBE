package com.sparta.bluemoon.service;

import com.sparta.bluemoon.dto.response.UserInfoDto;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //로그인한 유저 정보 가져오기
    public UserInfoDto isLogin(UserDetailsImpl userDetails) {
        return new UserInfoDto(userDetails);
    }
}
