package com.sparta.bluemoon.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오, 구글 이메일
    private String username;

    // 랜덤 닉네임
    private String nickname;

    private String password;

    @Column(length = 1000)
    private String token;


    public User(String email, String password, String nickname) {
        this.username = email;
        this.password = password;
        this.nickname = nickname;
    }


    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void registToken(String token){
        this.token = token;
    }
}
