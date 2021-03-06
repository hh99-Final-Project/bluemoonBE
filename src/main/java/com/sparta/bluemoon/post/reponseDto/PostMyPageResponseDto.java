package com.sparta.bluemoon.post.reponseDto;

import com.sparta.bluemoon.post.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostMyPageResponseDto {
    private String postUuid;
    private String nickname;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int count;

    public PostMyPageResponseDto(Post pagedPost, int count) {
        this.postUuid = pagedPost.getPostUuid();
        this.nickname = pagedPost.getUser().getNickname();
        this.userId = pagedPost.getUser().getId();
        this.title = pagedPost.getTitle();
        this.content = pagedPost.getContent();
        this.createdAt = pagedPost.getCreatedAt();
        this.count = count;
    }
}
