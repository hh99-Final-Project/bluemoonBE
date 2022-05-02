package com.sparta.bluemoon.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatMessageDto implements Serializable {

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 공통으로 만들어진 방 번호
    private Long userId; // 상대방 prvateKey
    private String message; // 메시지

    public ChatMessageDto() {
    }
}