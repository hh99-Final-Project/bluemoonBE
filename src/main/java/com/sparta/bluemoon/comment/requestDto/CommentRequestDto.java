package com.sparta.bluemoon.comment.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private String postUuid;
    private String content;
    private String parentUuid;
    private String timer;
    private boolean isLock;
}
