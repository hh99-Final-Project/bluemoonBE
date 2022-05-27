package com.sparta.bluemoon.point;

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
        int recommendCount = point.getRecommendCount();

        if(state.equals("POST_POINT")){
            myPoint+=500;
            postCount--;
        }

        else if(state.equals("COMMENT_POINT")){
            myPoint+=100;
            commentCount--;
        }

        else if(state.equals("LOTTO_POINT")){
            myPoint-=1000;
            if(recommendCount > 0){
                recommendCount--;
            }else{
                lottoCount--;
            }

        }

        point.update(myPoint, postCount, commentCount, lottoCount, recommendCount);
        pointRepository.save(point);
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
}

