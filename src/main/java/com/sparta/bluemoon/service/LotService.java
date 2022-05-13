package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Lot;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.PersonalInfoRequestDto;
import com.sparta.bluemoon.dto.response.LotResponseDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.repository.LotRepository;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.sparta.bluemoon.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LotService {

    private final PointService pointService;
    private final PointRepository pointRepository;
    private final LotRepository lotRepository;

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
            result = getLotResult(user);
        }
        //TODO:EXCEPTION만들기
        else {
            throw new CustomException(CANNOT_LOT);
        }

       return new LotResponseDto(result, userPoint);
    }



    //룰렛돌리기
    public boolean getLotResult(User user) {
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
                Lot lot = new Lot(user);
                lotRepository.save(lot);
            }

            return result;
        } else {
            System.out.println("바나나 없음");
            return false;
        }
    }

    @Transactional
    public void writePersonalInfo(UserDetailsImpl userDetails, PersonalInfoRequestDto requestDto){
        String nickname = requestDto.getNickname();
        if(!userDetails.getUser().getNickname().equals(nickname)){
            throw new CustomException(DOESNT_WRITE_OTHER_NICKNAME);
        }

        List<Lot> winners = lotRepository.findByNicknameAndPersonalInfo(nickname,false);
        if(winners.isEmpty()){
            throw new CustomException(NO_WINNER);
        } else{
            winners.get(0).updateInfo(requestDto.getPhoneNumber());
        }
    }
}
