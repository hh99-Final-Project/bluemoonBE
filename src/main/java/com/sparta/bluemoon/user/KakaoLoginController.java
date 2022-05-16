package com.sparta.bluemoon.user;

import com.sparta.bluemoon.user.responseDto.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakoLoginService;

//    @GetMapping(value = "/kakao/login")
//    public ResponseEntity<Object> requestAuthCodeFromKakao() {
//        return kakoLoginService.requestAuthCodeFromKakao();
//    }

//    @GetMapping(value = "/user/kakao/callback")
////    public ResponseEntity<KakaoLoginService> redirectKakaoLogin(
//    public ResponseEntity redirectKakaoLogin(@RequestParam(value = "code") String authCode) {
//        return kakoLoginService.login(authCode);
//    }
    //access 토큰을 받아야함.
    @PostMapping(value = "/api/login/kakao")
    public ResponseEntity kakaoLogin(@RequestBody SocialLoginRequestDto socialLoginRequestDto) {
        return kakoLoginService.login(socialLoginRequestDto.getJwtToken());
    }
}