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

    private String title;

    private String message;

    @ManyToOne
    private User user;

    public Alarm(ChatMessageDto chatMessageDto, User user) {
        this.title = chatMessageDto.getTitle();
        this.message = chatMessageDto.getMessage();
        this.user = user;
    }
}
