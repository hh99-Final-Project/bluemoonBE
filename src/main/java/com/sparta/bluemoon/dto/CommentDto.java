package com.sparta.bluemoon.dto;

import com.sparta.bluemoon.domain.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private boolean isShow;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isShow = comment.isShow();
    }
}
