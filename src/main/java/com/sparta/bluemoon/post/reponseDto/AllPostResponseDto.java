package com.sparta.bluemoon.post.reponseDto;

import com.sparta.bluemoon.post.Post;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllPostResponseDto {
    // 게시글 pk
    private String postUuid;

    // 글 작성자의 랜덤 닉네임
    private String nickname;

    // 글 작성자의 userId
    private Long userId;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    //voice 파일
    private String voiceUrl;

    //게시글 녹음 시간
    private String timer;

    // 게시글 작성 시간
    private LocalDateTime createdAt;

    public AllPostResponseDto(Post post) {
        this.postUuid = post.getPostUuid();
        this.nickname = post.getUser().getNickname();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.voiceUrl = post.getVoiceUrl();
        this.timer = post.getTimer();
        this.createdAt = post.getCreatedAt();
    }
}
