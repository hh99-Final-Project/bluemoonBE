package com.sparta.bluemoon.domain;

import com.sparta.bluemoon.dto.request.CommentRequestDto;

import javax.persistence.*;

import com.sparta.bluemoon.security.UserDetailsImpl;
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
public class Comment extends Timestamped{

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

    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails, Post post, String voiceUrl){
        this.content = commentRequestDto.getContent();
        this.post = post;
        this.user = userDetails.getUser();
        this.voiceUrl = voiceUrl;
        this.isShow = true;
    }
}
