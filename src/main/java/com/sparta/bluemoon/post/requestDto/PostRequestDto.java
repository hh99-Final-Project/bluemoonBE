package com.sparta.bluemoon.post.requestDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@Data
public class PostRequestDto {

    private String content;
    private MultipartFile file;
    private Long userId;
    private String nickName;

    public PostRequestDto(String content, MultipartFile file, String nickName, Long userId) {
        this.content = content;
        this.userId = userId;
        this.file = file;
        this.nickName = nickName;

    }
}

