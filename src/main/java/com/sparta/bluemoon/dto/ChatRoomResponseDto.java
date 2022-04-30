package com.sparta.bluemoon.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomResponseDto implements Comparable<ChatRoomResponseDto> {
    private String roomName;
    private Long roomId;
    private String lastMessage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    public ChatRoomResponseDto(String roomName, Long roomId, String lastMessage,LocalDateTime lastTime ) {
        this.roomName = roomName;
        this.roomId=roomId;
        this.lastMessage=lastMessage;
        this.createAt=lastTime;
    }

    @Override
    public int compareTo(ChatRoomResponseDto responseDto) {
        if (responseDto.createAt.isBefore(createAt)) {
            return 1;
        } else if (responseDto.createAt.isAfter(createAt)) {
            return -1;
        }
        return 0;
    }
}
