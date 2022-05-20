package com.sparta.bluemoon.post.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequestDto {

    private String title;

    private String content;

    private String timer;

    public PostCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
