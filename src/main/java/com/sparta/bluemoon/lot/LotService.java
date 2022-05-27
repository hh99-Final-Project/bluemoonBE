package com.sparta.bluemoon.lot;

import com.sparta.bluemoon.lot.Lot.PhoneNumberStatus;
import com.sparta.bluemoon.lot.requestDto.PersonalInfoRequestDto;
import com.sparta.bluemoon.lot.responseDto.LotResponseDto;
import com.sparta.bluemoon.point.Point;
import com.sparta.bluemoon.point.PointService;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static com.sparta.bluemoon.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LotService {

    private final PointService pointService;
    private final PointRepository pointRepository;
    private final LotRepository lotRepository;

    public static int bananaCount = 5;

   //당첨 여부 판단
   @Transactional
    public LotResponseDto doLot(User user){

        Point point = user.getPoint();
        int userPoint;
        boolean result;


        if (point.getLottoCount() != 0 && point.getMyPoint() >= 1000) {
            //포인트 변경,카운트 감소
            userPoint = pointService.pointChange(point, "LOTTO_POINT");
            result = getLotResult(user);
        }
        //TODO:EXCEPTION만들기
        else {
            if(point.getLottoCount()==0){
                throw new CustomException(LACK_OF_LOTTO_COUNT);
            } else if(point.getMyPoint()<1000){
                throw new CustomException(LACK_OF_POINT);
            }else{
                throw new CustomException(CANNOT_LOT);
            }
        }

       return new LotResponseDto(result, userPoint);
    }



    //룰렛돌리기
    public boolean getLotResult(User user) {
        if(bananaCount>0){
            Random r = new Random();
            boolean result = false;
            int temp = 0;

            temp = r.nextInt(100);

            //당첨 시:당첨 확률 10%
            if (temp >= 0 && temp < 20) {
                result = true;
                bananaCount--;
                Lot lot = new Lot(user, result, PhoneNumberStatus.PENDING);
                lotRepository.save(lot);
            } else {
                Lot lot = new Lot(user, result, PhoneNumberStatus.NOTREQUIRE);
                lotRepository.save(lot);
            }

            return result;
        } else {
            // System.out.println("바나나 없음");
            return false;
        }
    }

    //개인 정보 입력
    @Transactional
    public void writePersonalInfo(User user, PersonalInfoRequestDto requestDto){
       String checkPhoneNumber = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$";
       if(!requestDto.isPersonalInfo()){
           throw new CustomException(PERSONAL_INFO_DISAGREE);
       } else {
           String nickname = user.getNickname();
           List<Lot> winners = lotRepository.findByNicknameAndPersonalInfo(nickname, false);
           if(!Pattern.matches(checkPhoneNumber,requestDto.getPhoneNumber())){
               throw new CustomException(WRONG_FORMAT);
           }

           if (winners.isEmpty()) {
               throw new CustomException(NO_WINNER);
           } else {
               winners.get(0).updateInfo(requestDto);
           }
       }
    }
}
