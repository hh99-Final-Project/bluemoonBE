package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.response.LotResponseDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.sparta.bluemoon.exception.ErrorCode.CANNOT_LOT;

@Service
@RequiredArgsConstructor
public class LotService {

    private final PointService pointService;
    private final PointRepository pointRepository;

    public int bananaCount = 5;
   //판단
   @Transactional
    public LotResponseDto doLot(User user){

        Point point = pointRepository.findByUser(user);
        int userPoint;
        boolean result;

        if (point.getLottoCount() != 0 && point.getMyPoint() >= 1000) {
            //포인트 변경,카운트 감소
            userPoint = pointService.pointChange(point, "LOTTO_POINT");
            result = getLotResult();
        }
        //TODO:EXCEPTION만들기
        else {
            throw new CustomException(CANNOT_LOT);
        }

       return new LotResponseDto(result, userPoint);
    }



    //룰렛돌리기
    public boolean getLotResult() {
        System.out.println(bananaCount);
        if(bananaCount>0){
            Random r = new Random();
            boolean result = false;
            int temp = 0;

            temp = r.nextInt(100);
            System.out.println(temp);

            //당첨 시:당첨 확률 10%
            if (temp >= 0 && temp < 10) {
                result = true;
                bananaCount--;
            }

            return result;
        } else {
            System.out.println("바나나 없음");
            return false;
        }
    }
}
