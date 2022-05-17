//package com.sparta.bluemoon.service;
//
//import static org.assertj.core.api.Assertions.*;
//
//import com.sparta.bluemoon.comment.CommentService;
//import com.sparta.bluemoon.point.Point;
//import com.sparta.bluemoon.post.Post;
//import com.sparta.bluemoon.user.User;
//import com.sparta.bluemoon.comment.requestDto.CommentRequestDto;
//import com.sparta.bluemoon.post.PostService;
//import com.sparta.bluemoon.post.requestDto.PostCreateRequestDto;
//import com.sparta.bluemoon.point.PointRepository;
//import com.sparta.bluemoon.post.PostRepository;
//import com.sparta.bluemoon.user.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class PointServiceTest {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    PostService postService;
//    @Autowired
//    PointRepository pointRepository;
//    @Autowired
//    CommentService commentService;
//
//    @Test
//    @Order(1)
//    @DisplayName("user엔티티, point엔티티 db에 저장")
//    public void test() {
//
//        User user = new User("email", "password", "nickname");
//        userRepository.save(user);
//
//
//        int mypoint = 0;
//        int postCount = 1;
//        int commentCount = 5;
//        int lottoCount = 1;
//
//        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
//        pointRepository.save(point);
//
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("user의 기본 point 확인.")
//    public void userPoint1() {
//
//        User user = userRepository.findByUsername("email").get();
//
//        assertThat(user.getPoint().getMyPoint()).isEqualTo(0);
//        assertThat(user.getPoint().getPostCount()).isEqualTo(1);
//        assertThat(user.getPoint().getCommentCount()).isEqualTo(5);
//        assertThat(user.getPoint().getLottoCount()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("user가 게시글을 하나 작성했을 때 point 확인.")
//    public void userPoint2() {
//        // given
//
//        User user = userRepository.findByUsername("email").get();
//
//        PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto("title", "content");
//
//        // when
//        postService.create(postCreateRequestDto, "voiceUrl", user);
//
//        user = userRepository.findByUsername("email").get();
//
//
//        // then
//        assertThat(user.getPoint().getMyPoint()).isEqualTo(500);
//        assertThat(user.getPoint().getPostCount()).isEqualTo(0);
//        assertThat(user.getPoint().getCommentCount()).isEqualTo(5);
//        assertThat(user.getPoint().getLottoCount()).isEqualTo(1);
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("user가 댓글을 하나 작성했을 때 point 확인.")
//    public void userPoint3() {
//        // given
//
//        User user = userRepository.findByUsername("email").get();
//
//        Post post = postRepository.findByUser(user).get(0);
//        CommentRequestDto commentRequestDto = new CommentRequestDto();
//        commentRequestDto.setContent("댓글내용");
//        commentRequestDto.setPostUuid(post.getPostUuid());
//
//        // when
//        commentService.saveComment(commentRequestDto, user, "voiceUrl");
//
//        user = userRepository.findByUsername("email").get();
//
//
//        // then
//        assertThat(user.getPoint().getMyPoint()).isEqualTo(600);
//        assertThat(user.getPoint().getPostCount()).isEqualTo(0);
//        assertThat(user.getPoint().getCommentCount()).isEqualTo(4);
//        assertThat(user.getPoint().getLottoCount()).isEqualTo(1);
//    }
//}