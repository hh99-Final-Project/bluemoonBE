package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {

    private Long userId;
    private String nickname;

    public UserInfoDto(UserDetailsImpl userDetails) {
        this.userId = userDetails.getUser().getId();
        this.nickname = userDetails.getUser().getNickname();
    }
}
