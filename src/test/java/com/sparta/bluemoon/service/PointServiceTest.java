package com.sparta.bluemoon.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.PostRepository;
import com.sparta.bluemoon.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PointServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    CommentService commentService;

    @Test
    @Order(1)
    @DisplayName("user엔티티, point엔티티 db에 저장")
    public void test() {
        User user = new User("email", "password", "nickname");
        userRepository.save(user);

        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;

        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);
    }

    @Test
    @Order(2)
    @DisplayName("user의 기본 point 확인.")
    public void userPoint1() {
        User user = userRepository.findByUsername("email").get();
        assertThat(user.getPoint().getMyPoint()).isEqualTo(0);
        assertThat(user.getPoint().getPostCount()).isEqualTo(1);
        assertThat(user.getPoint().getCommentCount()).isEqualTo(5);
        assertThat(user.getPoint().getLottoCount()).isEqualTo(1);
    }
}