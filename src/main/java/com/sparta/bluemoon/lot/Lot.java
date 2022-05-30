package com.sparta.bluemoon.lot;

import com.sparta.bluemoon.lot.requestDto.PersonalInfoRequestDto;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.util.Timestamped;
import java.util.EnumSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Lot extends Timestamped {

    // 메시지 타입 : 입장, 채팅
    public enum PhoneNumberStatus {
        PENDING, COMPLETE, NOTREQUIRE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String phoneNumber;

    @Column
    private boolean personalInfo;

    @Column
    @Enumerated(EnumType.STRING)
    private PhoneNumberStatus phoneNumberStatus;

    public Lot(User user, boolean type, PhoneNumberStatus phoneNumberStatus) {
        this.nickname = user.getNickname();
        this.personalInfo = type;
        this.phoneNumberStatus = phoneNumberStatus;
    }

    public void updateInfo(PersonalInfoRequestDto requestDto) {
        this.phoneNumber=requestDto.getPhoneNumber();
        this.phoneNumberStatus = PhoneNumberStatus.COMPLETE;
    }
}
