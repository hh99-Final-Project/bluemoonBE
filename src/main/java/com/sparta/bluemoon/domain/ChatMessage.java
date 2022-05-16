package com.sparta.bluemoon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sparta.bluemoon.dto.ChatMessageDto;
import com.sparta.bluemoon.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ChatMessage extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 메세지 작성자
    @ManyToOne
    private User user;

    // 채팅 메세지 내용
    private String message;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public ChatMessage(User user, ChatMessageDto chatMessageDto, ChatRoom chatRoom) {
        this.user = user;
        this.message = chatMessageDto.getMessage();
        this.chatRoom = chatRoom;
    }
}
