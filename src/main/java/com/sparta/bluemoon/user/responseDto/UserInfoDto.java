package com.sparta.bluemoon.user.responseDto;

import com.sparta.bluemoon.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {

    private Long userId;
    private String nickname;
    private int myPoint;
    private int lottoCount;

    public UserInfoDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.myPoint = user.getPoint().getMyPoint();
        this.lottoCount = user.getPoint().getLottoCount() + user.getPoint().getRecommendCount();
    }
}
