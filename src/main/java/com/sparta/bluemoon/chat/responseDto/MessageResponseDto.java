package com.sparta.bluemoon.chat.responseDto;

import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDto {

    private String roomId;
    private String message;
    private String createdAt;
    private Long otherUserId;
    private Long userId;

    public MessageResponseDto(ChatMessageDto roomMessage) {

        this.roomId = roomMessage.getRoomId();
        this.message = roomMessage.getMessage();
        this.createdAt = roomMessage.getCreatedAt();
        this.otherUserId = roomMessage.getOtherUserId();
        this.userId = roomMessage.getUserId();
    }
}
