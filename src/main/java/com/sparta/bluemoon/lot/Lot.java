package com.sparta.bluemoon.lot;

import com.sparta.bluemoon.lot.requestDto.PersonalInfoRequestDto;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.util.Timestamped;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String phoneNumber;

    @Column
    private boolean personalInfo;


    public Lot(User user) {
        this.nickname = user.getNickname();
        this.personalInfo = false;
    }

    public void updateInfo(PersonalInfoRequestDto requestDto) {
        this.phoneNumber=requestDto.getPhoneNumber();
        this.personalInfo=true;
    }
}
