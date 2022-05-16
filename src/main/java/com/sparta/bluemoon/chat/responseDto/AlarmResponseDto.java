package com.sparta.bluemoon.chat.responseDto;

import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmResponseDto {

    private String postUuid;
    private String message;
    private String createdAt;
    private Long otherUserId;
    private Long messageId;
    private String type;

    public AlarmResponseDto(ChatMessageDto roomMessage) {
        this.type = "ALARM";
        this.postUuid = roomMessage.getPostUuid();
        this.message = roomMessage.getMessage();
        this.createdAt = roomMessage.getCreatedAt();
        this.otherUserId = roomMessage.getOtherUserId();
        this.messageId = roomMessage.getMessageId();

    }
}
