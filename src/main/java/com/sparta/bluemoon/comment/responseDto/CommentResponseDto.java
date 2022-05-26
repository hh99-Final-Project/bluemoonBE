package com.sparta.bluemoon.comment.responseDto;

import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.comment.requestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private String postUuid;
    private String nickname;
    private String content;
    private String createdAt;
    private String timer;
    private boolean isShow;
    private boolean isLock;
    private int point;


    public CommentResponseDto(CommentRequestDto commentRequestDto, User user, String dateResult, int point) {
        this.postUuid = commentRequestDto.getPostUuid();
        this.nickname = user.getNickname();
        this.content = commentRequestDto.getContent();
        this.createdAt = dateResult;
        this.isShow = true;
        this.timer = commentRequestDto.getTimer();
        //댓글 작성자가 설정한 값을 보내줌
        this.isLock = commentRequestDto.isLock();
        this.point= point;
    }
}
