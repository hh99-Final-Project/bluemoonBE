package com.sparta.bluemoon.dto.response;


import com.sparta.bluemoon.domain.User;

public class SocialLoginResponseDto {

    private Long userId;
    private String nickname;

    public SocialLoginResponseDto(User kakaoUser) {
        this.userId = kakaoUser.getId();
        this.nickname = kakaoUser.getNickname();
    }
}
