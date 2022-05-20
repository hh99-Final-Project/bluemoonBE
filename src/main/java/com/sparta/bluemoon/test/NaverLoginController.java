package com.sparta.bluemoon.test;

import com.sparta.bluemoon.user.requestDto.RefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/naver")
@RequiredArgsConstructor
public class NaverLoginController {

    private final ConfigUtils configUtils;
    private final NaverLoginService naverLoginService;

    @GetMapping(value = "/login")
    public ResponseEntity<Object> requestAuthCodeFromNaver() {
        return naverLoginService.requestAuthCodeFromNaver();
    }

    @GetMapping(value = "/login/redirect")
//    public ResponseEntity<NaverLoginService> redirectNaverLogin(
    public ResponseEntity redirectNaverLogin(
        @RequestParam(value = "code") String authCode
    ) {

        return naverLoginService.login(authCode);
    }

}