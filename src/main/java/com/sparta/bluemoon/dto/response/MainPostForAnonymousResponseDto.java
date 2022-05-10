package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainPostForAnonymousResponseDto {

    private String title;
    private String content;
    private String postUuid;
    private String nickname;

    public MainPostForAnonymousResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postUuid = post.getPostUuid();
        this.nickname = post.getUser().getNickname();
    }
}