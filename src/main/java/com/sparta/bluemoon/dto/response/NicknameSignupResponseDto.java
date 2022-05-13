package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Point;
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
