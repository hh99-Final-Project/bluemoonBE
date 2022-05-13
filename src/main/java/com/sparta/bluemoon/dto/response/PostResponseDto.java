package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {

    private String postUuid;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private List<CommentDto> comments;
    private String voiceUrl;
    private String timer;
    private LocalDateTime createdAt;
    private boolean isLock;
    private boolean isShow;

    public PostResponseDto(Post post, List<CommentDto> commentList) {
        this.postUuid = post.getPostUuid();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.comments = commentList;
        this.voiceUrl = post.getVoiceUrl();
        this.timer = post.getTimer();
        this.createdAt = post.getCreatedAt();
        this.isShow = post.isShow();
    }
}
