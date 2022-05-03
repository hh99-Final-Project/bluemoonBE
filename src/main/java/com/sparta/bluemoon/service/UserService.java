package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.NicknameSignupRequestDto;
import com.sparta.bluemoon.dto.response.UserInfoDto;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    public boolean isDuplicated(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    //로그인한 유저 정보 가져오기
    public UserInfoDto isLogin(UserDetailsImpl userDetails) {
        return new UserInfoDto(userDetails);
    }

    //로그인한 유저에 닉네임 정보 입력하기
    @Transactional
    public void signupNickname(NicknameSignupRequestDto nicknameSignupRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.")
        );
        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;
        Point point = new Point(mypoint, user,postCount,commentCount,lottoCount);
        pointRepository.save(point);
        user.changeNickname(nicknameSignupRequestDto.getNickname());

    }
}
