package com.sparta.bluemoon.point;

import com.sparta.bluemoon.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter

public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int myPoint;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int postCount;
    private int commentCount;
    private int lottoCount;
    //추천인 적으면 카운트 1개 추가해줌. 단 스케쥴러에 의해 초기화 되지 않게 하기 위해 새로운 컬럼 생성
    private int recommendCount;

    public Point(int mypoint, User user, int postCount, int commentCount, int lottoCount, int recommendCount) {
        this.myPoint = mypoint;
        this.user = user;
        this.postCount=postCount;
        this.commentCount=commentCount;
        this.lottoCount=lottoCount;
        this.recommendCount = recommendCount;
    }

    public void update(int point, int postCount, int commentCount, int lottoCount, int recommendCount) {
        this.myPoint = point;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.lottoCount = lottoCount;
        this.recommendCount = recommendCount;
    }

    public void eventPoint(int point) {
        this.myPoint = point;
    }

    public void recommendCount(int count){
        this.recommendCount = count;
    }

    public void resetCount() {
        this.postCount = 1;
        this.commentCount = 5;
        this.lottoCount = 1;
    }

}
