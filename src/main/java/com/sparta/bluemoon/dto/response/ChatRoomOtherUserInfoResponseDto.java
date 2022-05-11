package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.User;
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
