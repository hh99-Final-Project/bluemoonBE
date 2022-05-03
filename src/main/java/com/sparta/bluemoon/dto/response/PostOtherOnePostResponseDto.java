package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOtherOnePostResponseDto {
    // 게시글 pk
    private String postId;

    // 글 작성자의 랜덤 닉네임
    private String nickname;

    // 글 작성자의 userId
    private Long userId;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    // 게시글 작성 시간
    private LocalDateTime createdAt;

    public PostOtherOnePostResponseDto(Post post) {
        this.postId = post.getPostUuid();
        this.nickname = post.getUser().getNickname();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
    }
}
