package com.sparta.bluemoon.post.reponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateResponseDto {
    private String voiceUrl;
    private int point;

    public PostCreateResponseDto(String voiceUrl, int userPoint) {
        this.voiceUrl = voiceUrl;
        this.point = userPoint;
    }



}
