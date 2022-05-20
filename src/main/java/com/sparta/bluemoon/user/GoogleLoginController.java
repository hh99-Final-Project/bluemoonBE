package com.sparta.bluemoon.user;

import com.sparta.bluemoon.user.requestDto.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    //tokenId를 받아야함.
    @PostMapping(value = "/api/login/google")
    public ResponseEntity googleLogin(@RequestBody SocialLoginRequestDto socialLoginRequestDto) {
        return googleLoginService.login(socialLoginRequestDto.getJwtToken());
    }


}