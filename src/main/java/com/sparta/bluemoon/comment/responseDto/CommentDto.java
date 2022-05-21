package com.sparta.bluemoon.comment.responseDto;

import com.sparta.bluemoon.comment.Comment;
import com.sparta.bluemoon.comment.DeleteStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private Long userId;
    private String commentUuid;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private String voiceUrl;
    private String timer;
    private boolean isShow;
    private boolean isLock;
    private List<CommentDto> children = new ArrayList<>();
    private DeleteStatus isDeleted;

    public CommentDto(Comment comment) {
        this.userId = comment.getUser().getId();
        this.commentUuid = comment.getCommentUuid();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.voiceUrl = comment.getVoiceUrl();
        this.timer = comment.getTimer();
        this.isLock = comment.isLock();
        //댓글 작성자이면 false를 보내줌
        this.isShow = comment.isShow();
        this.isDeleted = comment.getIsDeleted();
    }
}
