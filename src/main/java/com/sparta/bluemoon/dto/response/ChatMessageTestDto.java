package com.sparta.bluemoon.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.bluemoon.domain.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class ChatMessageTestDto {

    private Long userId;
    private String nickname;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createdAt;

    public ChatMessageTestDto(ChatMessage chatMessage) {
        this.userId = chatMessage.getUser().getId();
        this.nickname = chatMessage.getUser().getNickname();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
