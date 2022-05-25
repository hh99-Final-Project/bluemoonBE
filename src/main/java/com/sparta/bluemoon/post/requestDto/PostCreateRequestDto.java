package com.sparta.bluemoon.post.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotBlank(message = "제목을 입력해 주십시오")
    private String title;

    @NotBlank(message = "게시글 내용을 입력해 주십시오")
    private String content;

    private String timer;

}
