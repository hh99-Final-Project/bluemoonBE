package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.repository.CommentRepository;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    //포인트 주기
    @Transactional
    public int pointChange(Point point, String state){

        int myPoint = point.getMyPoint();
        int postCount = point.getPostCount();
        int commentCount = point.getCommentCount();
        int lottoCount = point.getLottoCount();

        if(state=="POST_POINT"){
            myPoint+=500;
            postCount--;
        }

        else if(state=="COMMENT_POINT"){
            myPoint+=100;
            commentCount--;
        }

        else if(state=="LOTTO_POINT"){
            myPoint-=1000;
            lottoCount--;
        }

        point.update(myPoint, postCount,commentCount,lottoCount);

        //포인트 값만 뿌려주기...?
        return myPoint;
    }


    //카운트 업데이트 변경감지 자동 db저장
    @Transactional
    public void countReset() {

        List<Point> points = pointRepository.findAll();

        for(Point point:points){
            //업데이트
            point.resetCount();
        }
    }


//    //추첨 돌렸을 때
//    @Transactional
//    public void lotto(User user){
//        int point = user.getPoint();
//
//        if(point<1000){
//            throw new IllegalArgumentException("1000 포인트 부터 추첨할 수 있습니다.");
//        }
//        // 추첨 과정
//
//    }

}

