package com.sparta.bluemoon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오, 구글 이메일
    private String username;

    // 랜덤 닉네임
    private String nickname;

    private String password;


    public User(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public void createNickname(String nickname) {
        this.nickname = nickname;
    }
}
