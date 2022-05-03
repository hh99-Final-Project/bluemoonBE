package com.sparta.bluemoon.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int myPoint;

    @OneToOne//조인 컬럼..?
    @JoinColumn(name = "user_id")
    private User user;

    private int postCount = 0;
    private int commentCount = 0;
    private int lottoCount = 0;

    public void update(int point, int postCount, int commentCount, int lottoCount) {
        this.myPoint = point;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.lottoCount = lottoCount;
    }

    public void resetCount() {
        this.postCount = 1;
        this.commentCount = 5;
        this.lottoCount = 0;
    }

}
