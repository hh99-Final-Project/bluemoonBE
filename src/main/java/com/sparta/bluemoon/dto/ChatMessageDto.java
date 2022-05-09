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

//    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private Long id;
    private MessageType type; // 메시지 타입
    private String roomId; // 공통으로 만들어진 방 번호
    private Long userId; // type = talk 면 발신인 id, enter면 수신인 id
    private String title;
    private String postUuid;
    private String message; // 메시지
    private String createdAt;

}