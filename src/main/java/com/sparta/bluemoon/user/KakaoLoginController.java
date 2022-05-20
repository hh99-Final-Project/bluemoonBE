package com.sparta.bluemoon.user;

import com.sparta.bluemoon.user.requestDto.RefreshTokenDto;
import com.sparta.bluemoon.user.requestDto.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakoLoginService;

    @PostMapping(value = "/api/login/kakao")
    public ResponseEntity kakaoLogin(@RequestBody SocialLoginRequestDto socialLoginRequestDto) {
        return kakoLoginService.login(socialLoginRequestDto.getJwtToken());
    }
}