package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {

    private String postId;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private List<CommentDto> comments;
    private String voiceUrl;
    private boolean isShow;

    public PostResponseDto(Post post1, List<CommentDto> commentList) {
        this.postId = post1.getPostUuid();
        this.userId = post1.getUser().getId();
        this.nickname = post1.getUser().getNickname();
        this.title = post1.getTitle();
        this.content = post1.getContent();
        this.comments = commentList;
        this.voiceUrl = post1.getVoiceUrl();
        this.isShow = post1.isShow();
    }
}
