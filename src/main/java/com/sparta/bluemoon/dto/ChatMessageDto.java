package com.sparta.bluemoon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private Long id;
    private MessageType type; // 메시지 타입
    private String roomId; // 공통으로 만들어진 방 번호
    private Long otherUserId; // 상대방
    private String title;
    private String postUuid;
    private String message; // 메시지
    private String createdAt;

    public ChatMessageDto(ChatMessageDto chatMessageDto, String message, String username) {
        this.type = MessageType.ENTER; // 메시지 타입
        this.roomId = chatMessageDto.roomId; // 방 이름
        this.otherUserId = chatMessageDto.otherUserId; // 상대방 prvateKey
        this.message = message; // 메시지
    }
}