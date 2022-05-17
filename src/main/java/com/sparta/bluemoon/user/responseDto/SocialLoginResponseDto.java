package com.sparta.bluemoon.user.responseDto;


import com.sparta.bluemoon.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginResponseDto {

    private Long userId;
    private String nickname;
    private int myPoint;
    private int lottoCount;

    public SocialLoginResponseDto(User kakaoUser) {
        this.userId = kakaoUser.getId();
        this.nickname = kakaoUser.getNickname();
        this.myPoint = kakaoUser.getPoint() == null ? 0 : kakaoUser.getPoint().getMyPoint();
        this.lottoCount = kakaoUser.getPoint() == null ? 1 : kakaoUser.getPoint().getLottoCount();
    }
}
