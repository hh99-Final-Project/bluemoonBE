package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.dto.ChatMessageDto;
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

    public AlarmResponseDto(ChatMessageDto roomMessage) {
        this.postUuid = roomMessage.getPostUuid();
        this.message = roomMessage.getMessage();
        this.createdAt = roomMessage.getCreatedAt();
        this.otherUserId = roomMessage.getOtherUserId();
        this.messageId = roomMessage.getMessageId();
    }
}
