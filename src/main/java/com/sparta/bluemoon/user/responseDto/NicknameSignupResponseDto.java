package com.sparta.bluemoon.user.responseDto;

import com.sparta.bluemoon.point.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameSignupResponseDto {
    private int myPoint;

    public NicknameSignupResponseDto(Point userPoint) {
        this.myPoint= userPoint.getMyPoint();
    }
}
