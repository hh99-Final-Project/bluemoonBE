package com.sparta.bluemoon.comment.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private String postUuid;

    @NotBlank(message = "댓글 내용을 입력해 주십시오")
    private String content;

    private String parentUuid;

    private String timer;

    private boolean isLock;
}
