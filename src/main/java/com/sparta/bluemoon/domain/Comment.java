package com.sparta.bluemoon.domain;

import com.sparta.bluemoon.dto.request.CommentRequestDto;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 댓글 내용
    private String content;

    private boolean isShow;
    //true가 잠금 false가 공개
    private boolean isLock;
    //댓글 음성파일
    private String voiceUrl;
    //녹음 파일 시간
    private String timer;

    //commentId 대체할 UUID 생성
    @Column(unique = true)
    private String commentUuid = UUID.randomUUID().toString();

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // 상위 댓글
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 하위 댓글
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    // 삭제 여부
    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    //requestDto가 비어있을경우 빈칸으로 처리
    public Comment(CommentRequestDto commentRequestDto, User user, Post post, String voiceUrl, Comment parentComment){
        this.content = (commentRequestDto == null ? "" : commentRequestDto.getContent());
        this.post = post;
        this.user = user;
        this.voiceUrl = voiceUrl;
        this.timer = (commentRequestDto == null ? "" : commentRequestDto.getTimer());
        this.isShow = false;
        this.isLock = commentRequestDto.isLock();
        this.parent = parentComment;
        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
        this.isDeleted = DeleteStatus.N;
    }

    public void changeDeletedStatus(DeleteStatus type) {
        this.isDeleted = type;
    }
}
