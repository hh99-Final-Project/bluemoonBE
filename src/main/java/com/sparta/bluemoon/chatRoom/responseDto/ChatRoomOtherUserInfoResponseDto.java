package com.sparta.bluemoon.chatRoom.responseDto;

import com.sparta.bluemoon.user.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatRoomOtherUserInfoResponseDto {

    private Long otherUserId;
    private String otherUserNickname;

    public ChatRoomOtherUserInfoResponseDto(User otherUser) {
        this.otherUserId = otherUser.getId();
        this.otherUserNickname = otherUser.getNickname();
    }
}
