package com.sparta.bluemoon.dto.response;

public class PostCreateResponseDto {
    private String voiceUrl;
    private int point;

    public PostCreateResponseDto(String voiceUrl, int userPoint) {
        this.voiceUrl = voiceUrl;
        this.point = userPoint;
    }
}
