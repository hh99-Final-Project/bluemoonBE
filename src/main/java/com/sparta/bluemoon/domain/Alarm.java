package com.sparta.bluemoon.domain;

import com.sparta.bluemoon.dto.ChatMessageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; //어느 게시글에 댓글을 달았는지 확인하기 위해

    private String message;

    @ManyToOne
    private User user;

    public Alarm(ChatMessageDto chatMessageDto, User user) {
        this.title = chatMessageDto.getTitle();
        this.message = chatMessageDto.getMessage();
        this.user = user;
    }
}
