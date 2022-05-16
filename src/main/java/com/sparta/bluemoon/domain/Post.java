package com.sparta.bluemoon.domain;

import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    //게시글 음성파일
    private String voiceUrl;
    //녹음 파일 시간
    private String timer;

    private boolean isShow;

    //postId 대체할 UUID 생성
    @Column(unique = true)
    private String postUuid = UUID.randomUUID().toString();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    private User user;

    //requestDto가 비어있을경우 빈칸으로 처리
    public Post(PostCreateRequestDto requestDto, String voiceUrl, User user) {
        this.title = (requestDto == null ? "" : requestDto.getTitle());
        this.content = (requestDto == null ? "" : requestDto.getContent());
        this.timer = (requestDto == null ? "" : requestDto.getTimer());
        this.voiceUrl = voiceUrl;
        this.user = user;
    }


    public Post(PostCreateRequestDto postCreateRequestDto, User user) {
        this.title = postCreateRequestDto.getTitle();
        this.content = postCreateRequestDto.getContent();
        this.user = user;
    }
}
