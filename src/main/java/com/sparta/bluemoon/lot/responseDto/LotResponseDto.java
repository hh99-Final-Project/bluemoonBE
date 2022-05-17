package com.sparta.bluemoon.lot.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotResponseDto {
    private boolean isResult;
    private int point;

    public LotResponseDto(boolean result, int userPoint) {
        this.isResult = result;
        this.point = userPoint;
    }
    //int lottoCount;
}
