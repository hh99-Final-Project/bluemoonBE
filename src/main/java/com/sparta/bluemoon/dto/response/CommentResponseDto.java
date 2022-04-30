package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.dto.request.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long postId;
    private String nickname;
    private String content;
    private String createdAt;
    private boolean isShow;

    public CommentResponseDto(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails, String dateResult) {
        this.postId = commentRequestDto.getPostId();
        this.nickname = userDetails.getUser().getNickname();
        this.content = commentRequestDto.getContent();
        this.createdAt = dateResult;
        this.isShow = true;
    }
}