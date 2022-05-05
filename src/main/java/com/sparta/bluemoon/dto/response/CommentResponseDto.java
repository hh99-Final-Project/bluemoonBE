package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
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
    private boolean isShow;
    private int point;


    public CommentResponseDto(CommentRequestDto commentRequestDto, User user, String dateResult, int point) {
        this.postUuid = commentRequestDto.getPostUuid();
        this.nickname = user.getNickname();
        this.content = commentRequestDto.getContent();
        this.createdAt = dateResult;
        this.isShow = true;
        this.point= point;
    }
}
