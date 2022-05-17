package com.sparta.bluemoon.chat.responseDto;

import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreadMessageCount {

    private Long otherUserId;
    private int unreadCount;
    private String roomId;
    private String type;

    public UnreadMessageCount(ChatMessageDto roomMessage) {
        this.type = "UNREAD";
        this.otherUserId = roomMessage.getOtherUserId();
        this.unreadCount = roomMessage.getCount();
        this.roomId = roomMessage.getRoomId();
    }
}
