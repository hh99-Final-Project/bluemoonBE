package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;




//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest {

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
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentQuerydslRepository commentQuerydslRepository;
    @Autowired
    PointService pointService;


    @Test
    ///@BeforeEach
    public void saveUser() {

        User user = new User("123@123","김승민","1111");
        userRepository.save(user);

        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;

        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);
    }

//    @Test
//    public void users(){
//        saveUser("123@123","1111","김승민");
//        saveUser("456@456","2222","김승현");
//        saveUser("789@789","3333","김아연");
//    }

    @Test
    void getOnePost() {
        //given

    }

    @Test
    void create() {
        //given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("제목","내용");
        User user1 = userRepository.findById(1L).orElseThrow(
                ()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.")
        );
        //when 게시글 작성하면
        postService.create(requestDto,"",user1);

        //then
        Post post = postRepository.findByUser(user1).get(0);
        assertEquals(post.getTitle(),"제목");
        assertEquals(post.getContent(),"내용");
        assertEquals(post.getUser().getNickname(),"김승민");
        assertEquals(post.getUser().getPoint().getMyPoint(),500);
        assertEquals(post.getUser().getPoint().getPostCount(),0);
    }

    @Test
    void delete() {

    }

    @Test
    void findOneMyPage() {
    }

    @Test
    void findOtherUserPosts() {
    }

    @Test
    void getMainPost() {
    }

    @Test
    void getMainDetailPost() {
    }
}