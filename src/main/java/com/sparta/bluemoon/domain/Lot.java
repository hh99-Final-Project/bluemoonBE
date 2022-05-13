package com.sparta.bluemoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void updateInfo(String phoneNumber) {
        this.phoneNumber=phoneNumber;
        this.personalInfo=true;
    }
}
