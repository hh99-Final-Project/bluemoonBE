package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.response.UserInfoDto;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("유저 저장하기")
    void saveUser() {
        User user = new User("1234", "가나다라", "123456");
        userRepository.save(user);

        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;
        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);

        assertEquals(userRepository.findById(1L).get().getId(), user.getId());
        assertEquals(pointRepository.findByUser(user).getMyPoint(), 0);
    }

    @Test
    @DisplayName("닉네임 중복확인")
    void duplicate(){
        String nickname = "가나다라";
        assertFalse(userService.isDuplicated(nickname));
    }

    @Test
    @DisplayName("유저정보 가져오기")
    void islogin(){
        User user = userRepository.findById(1L).get();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UserInfoDto userInfoDto = userService.isLogin(userDetails);

        assertEquals(userInfoDto.getUserId(), 1L);
        assertEquals(userInfoDto.getNickname(), "가나다라");
        assertEquals(userInfoDto.getMyPoint(), 0);
        assertEquals(userInfoDto.getLottoCount(), 1);
    }
}