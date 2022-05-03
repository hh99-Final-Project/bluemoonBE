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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용
    private String content;

    private boolean isShow;

    //댓글 음성파일
    private String voiceUrl;

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
    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails, Post post, String voiceUrl, Comment parentComment){
        this.content = (commentRequestDto == null ? "" : commentRequestDto.getContent());
        this.post = post;
        this.user = userDetails.getUser();
        this.voiceUrl = voiceUrl;
        this.isShow = true;
        this.parent = parentComment;
        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
        this.isDeleted = DeleteStatus.N;
    }

    public void changeDeletedStatus(DeleteStatus type) {
        System.out.println(this.isDeleted);
        this.isDeleted = type;
        System.out.println(this.isDeleted);
    }
}
