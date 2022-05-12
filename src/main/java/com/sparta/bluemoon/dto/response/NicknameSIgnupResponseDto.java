package com.sparta.bluemoon.dto.response;

import com.sparta.bluemoon.domain.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameSIgnupResponseDto {
    private int myPoint;

    public NicknameSIgnupResponseDto(Point userPoint) {
        this.myPoint= userPoint.getMyPoint();
    }
}
