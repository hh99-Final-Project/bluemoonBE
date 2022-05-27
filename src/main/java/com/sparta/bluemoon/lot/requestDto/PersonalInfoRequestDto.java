package com.sparta.bluemoon.lot.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor//테스트 코드용
@Getter
@Setter
public class PersonalInfoRequestDto {

    //private String nickname;
    private String phoneNumber;
    private boolean isPersonalInfo;
}
