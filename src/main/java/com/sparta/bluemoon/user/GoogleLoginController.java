package com.sparta.bluemoon.user;

import com.sparta.bluemoon.user.responseDto.SocialLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

//    @GetMapping(value = "/login")
//    public ResponseEntity<Object> requestAuthCodeFromGoogle() {
//        return googleLoginService.requestAuthCodeFromGoogle();
//    }

//    @GetMapping(value = "/login/redirect")
//    public ResponseEntity redirectGoogleLogin(
//            @RequestParam(value = "code") String authCode
//    ) {
//        return googleLoginService.login(authCode);
//    }
    //tokenId를 받아야함.
    @PostMapping(value = "/api/login/google")
    public ResponseEntity googleLogin(@RequestBody SocialLoginRequestDto socialLoginRequestDto) {
        System.out.println(socialLoginRequestDto.getJwtToken());
        return googleLoginService.login(socialLoginRequestDto.getJwtToken());
    }
}