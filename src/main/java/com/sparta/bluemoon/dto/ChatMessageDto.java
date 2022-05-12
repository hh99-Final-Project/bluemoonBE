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
        ENTER, TALK, UNREAD_MESSAGE_COUNT
    }

    private Long messageId;
    private MessageType type; // 메시지 타입
    private String roomId; // 공통으로 만들어진 방 번호
    private Long otherUserId; // 상대방
    private String title;    //게시글 제목
    private String postUuid; //게시글 uuid
    private String message; // 메시지
    private String createdAt;
    private Long userId;
    private int count;

    public ChatMessageDto(ChatMessageDto chatMessageDto, int count) {
        this.type = MessageType.UNREAD_MESSAGE_COUNT; // 메시지 타입
        this.roomId = chatMessageDto.roomId; // 방 이름
        this.otherUserId = chatMessageDto.otherUserId; // 상대방 prvateKey
        this.count = count; //안읽은 메세지 개수
    }
}