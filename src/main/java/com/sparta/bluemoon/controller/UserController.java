package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.NicknameCheckRequestDto;
import com.sparta.bluemoon.dto.request.NicknameSignupRequestDto;
import com.sparta.bluemoon.dto.response.NicknameSIgnupResponseDto;
import com.sparta.bluemoon.dto.response.UserInfoDto;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //로그인한 유저 정보 가져오기
    @GetMapping("/api/user/islogin")
    public UserInfoDto isLogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.isLogin(userDetails);
    }

    // 닉네임 중복 확인
    @PostMapping("/api/nicknames")
    public boolean isDuplicated(@RequestBody NicknameCheckRequestDto nicknameCheckRequestDto) {
        return userService.isDuplicated(nicknameCheckRequestDto.getNickname());
    }

    //로그인한 유저에 닉네임 정보 입력하기
    @PostMapping("/api/user/nickname")
    public NicknameSIgnupResponseDto signupNickname(@RequestBody NicknameSignupRequestDto nicknameSignupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return userService.signupNickname(nicknameSignupRequestDto, userDetails);
    }

    @GetMapping("/api/test")
    public String test(){
        System.out.println("요청왔음");
        return "정보가 잘 가나요?";
    }
}
