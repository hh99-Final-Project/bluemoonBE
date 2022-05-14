package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Alarm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmDto {

    private String message;

    private String postUuid;

    private Long messageId;

    public AlarmDto(Alarm alarm) {
        this.message = alarm.getMessage();
        this.postUuid = alarm.getPostUuid();
        this.messageId = alarm.getId();
    }
}
