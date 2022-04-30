package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMyPageResponseDto {
    private Long postId;
    private String nickname;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public PostMyPageResponseDto(Post pagedPost) {
        this.postId = pagedPost.getId();
        this.nickname = pagedPost.getUser().getNickname();
        this.userId = pagedPost.getUser().getId();
        this.title = pagedPost.getTitle();
        this.content = pagedPost.getContent();
        this.createdAt = pagedPost.getCreatedAt();
    }
}
