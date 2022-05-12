package com.sparta.bluemoon.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameSignupRequestDto {

    private String nickname;

    private String recommender;// 추천인 닉네임
}


