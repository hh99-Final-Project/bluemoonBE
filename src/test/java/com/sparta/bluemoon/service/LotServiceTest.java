package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Lot;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.PersonalInfoRequestDto;
import com.sparta.bluemoon.dto.response.LotResponseDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.exception.ErrorCode;
import com.sparta.bluemoon.repository.LotRepository;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.emitter.Emitter;

import java.util.List;

import static com.sparta.bluemoon.exception.ErrorCode.LACK_OF_POINT;
import static com.sparta.bluemoon.exception.ErrorCode.NO_WINNER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LotServiceTest {


    @Autowired
    PointService pointService;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    LotRepository lotRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LotService lotService;


   // @BeforeEach
   @Test
   @Order(1)
    public void test() {
        User user = new User("김승민", "TodayIsYolo", "1234");
        userRepository.save(user);

        int mypoint = 1000;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;

        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);
    }

    @Test
    @Order(2)
    @DisplayName("유저가 룰렛을 돌렸을 때")
    void doLot_normal() {
        //given
        User user = userRepository.findByNickname("TodayIsYolo").orElseThrow(
                ()-> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
        System.out.println("바나나"+ LotService.bananaCount);


        //when:로또 돌리면
        LotResponseDto responseDto = lotService.doLot(user);
        user = userRepository.findByNickname("TodayIsYolo").orElseThrow(
                ()-> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
        //then:룰렛 돌리면 카운트 감소, 포인트 감소
        assertEquals(responseDto.getPoint(),user.getPoint().getMyPoint());
        assertEquals(0,user.getPoint().getLottoCount());
        if(responseDto.isResult()){
            //당첨이면  바나나 감소 lot 에 저장
            assertEquals(4, LotService.bananaCount);
            List<Lot> lots = lotRepository.findByNicknameAndPersonalInfo("TodayIsYolo",false);
            assertFalse(lots.isEmpty());
        }else{
            assertEquals(5, LotService.bananaCount);
        }
    }



    @Test
    @Order(3)
    @DisplayName("당첨 시 개인 정보 저장")

    void writePersonalInfo() {
        //given

        //위에서 당첨 되면 에러나려나..?
        User user = userRepository.findByNickname("TodayIsYolo").orElseThrow(
                ()-> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
        //당첨이라고 가정
        Lot lot = new Lot(user);
        lotRepository.save(lot);
        PersonalInfoRequestDto personalInfoRequestDto = new PersonalInfoRequestDto("TodayIsYolo","123-1234-1234");
        //when
        lotService.writePersonalInfo(user ,personalInfoRequestDto);
        List<Lot> lots = lotRepository.findByNicknameAndPersonalInfo("TodayIsYolo",true);
        //then
        assertEquals(lots.get(0).getNickname(),personalInfoRequestDto.getNickname());
        assertEquals(lots.get(0).getPhoneNumber(),personalInfoRequestDto.getPhoneNumber());

    }
}


