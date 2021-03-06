package com.sparta.bluemoon.user;

import com.sparta.bluemoon.util.Timestamped;
import javax.persistence.*;


import com.sparta.bluemoon.point.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;


@Entity
@NoArgsConstructor
@Getter
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 카카오, 구글 이메일
    private String username;

    // 랜덤 닉네임
    private String nickname;

    private String password;

    private String type;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    //포인트
    @OneToOne(mappedBy = "user")
    private Point point;


    public User(String email, String password, String nickname, String type) {
        this.username = email;
        this.password = password;
        this.nickname = nickname;
        this.type = type;
        this.role = UserRoleEnum.USER;
    }

    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = UserRoleEnum.USER;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }


}
