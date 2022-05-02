package com.sparta.bluemoon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private Long id;
    private Long userId;
    private String imageUrl;
    private String fileName;
    private String content;
    private String nickName;


    @Builder
    public PostDto(String fileName, String imageUrl){
        this.imageUrl = imageUrl;
        this.fileName = fileName;
    }

    public PostDto(String fileName, String imageUrl, String content, String nickName, Long userId ){
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.content = content;
        this.nickName = nickName;
        this.userId = userId;
    }


}