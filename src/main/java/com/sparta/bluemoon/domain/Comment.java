package com.sparta.bluemoon.domain;

import com.sparta.bluemoon.dto.request.CommentRequestDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails, Post post){
        this.content = commentRequestDto.getContent();
        this.post = post;
        this.user = userDetails.getUser();
        this.isShow = true;
    }
}
