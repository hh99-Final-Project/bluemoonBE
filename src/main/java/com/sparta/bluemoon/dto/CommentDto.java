package com.sparta.bluemoon.dto;

import com.sparta.bluemoon.domain.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private String commentUuid;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private String voiceUrl;
    private boolean isShow;
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Comment comment) {
        this.commentUuid = comment.getCommentUuid();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.voiceUrl = comment.getVoiceUrl();
        this.isShow = comment.isShow();
    }
}
