//package com.sparta.bluemoon.service;
//
//import com.sparta.bluemoon.chat.Alarm;
//import com.sparta.bluemoon.chat.AlarmService;
//import com.sparta.bluemoon.user.User;
//import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
//import com.sparta.bluemoon.chat.responseDto.AlarmDto;
//import com.sparta.bluemoon.chat.AlarmRepository;
//import com.sparta.bluemoon.user.UserRepository;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class AlarmServiceTest {
//
//    @Autowired
//    private AlarmService alarmService;
//
//    @Autowired
//    private AlarmRepository alarmRepository;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void getAlarms() {
//        //given
//        User user = new User("123@123", "김승민", "1234");
//        userRepository.save(user);
//        ChatMessageDto chatMessageDto = new ChatMessageDto();
//
//        User user1 = userRepository.findByNickname("김승민").get();
//
//        for(int i = 0; i < 10; i++){
//            chatMessageDto.setTitle("알람제목" + i);
//            chatMessageDto.setMessage("알람메세지" + i);
//            chatMessageDto.setPostUuid("포스트번호" + i);
//            alarmRepository.save(new Alarm(chatMessageDto, user1));
//        }
//
//        //when
//        List<AlarmDto> alarmDtos = alarmService.getAlarms(0, user);
//        //then
//        //내림차순 정렬이기 때문에 거꾸로 검사한다
//        for(int i = 0; i <= 9; i++){
//            assertEquals(alarmDtos.get(i).getMessage(), "알람메세지" + (9 - i));
//            System.out.println(alarmDtos.get(i).getMessage() + " = " + "알람메세지" + (9-i));
//        }
//    }
//}