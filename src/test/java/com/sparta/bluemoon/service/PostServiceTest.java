//package com.sparta.bluemoon.service;
//
//import com.sparta.bluemoon.comment.CommentQuerydslRepository;
//import com.sparta.bluemoon.comment.CommentRepository;
//import com.sparta.bluemoon.comment.CommentService;
//import com.sparta.bluemoon.point.Point;
//import com.sparta.bluemoon.point.PointRepository;
//import com.sparta.bluemoon.point.PointService;
//import com.sparta.bluemoon.post.Post;
//import com.sparta.bluemoon.user.User;
//import com.sparta.bluemoon.post.PostRepository;
//import com.sparta.bluemoon.post.PostService;
//import com.sparta.bluemoon.post.requestDto.PostCreateRequestDto;
//import com.sparta.bluemoon.user.UserRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.sparta.bluemoon.comment.requestDto.CommentRequestDto;
//import com.sparta.bluemoon.post.reponseDto.MainPostForAnonymousResponseDto;
//import com.sparta.bluemoon.post.reponseDto.PostMyPageResponseDto;
//import com.sparta.bluemoon.post.reponseDto.PostOtherOnePostResponseDto;
//import com.sparta.bluemoon.post.reponseDto.PostResponseDto;
//import com.sparta.bluemoon.security.UserDetailsImpl;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//
//
////@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class PostServiceTest {
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
//    @Autowired
//    CommentRepository commentRepository;
//    @Autowired
//    CommentQuerydslRepository commentQuerydslRepository;
//    @Autowired
//    PointService pointService;
//
//
//
////    @Test
//////    @BeforeEach
////    @DisplayName("?????? ?????? ??????")
////    public void saveUser() {
////
////        User user = new User("123@123", "?????????", "111");
////        userRepository.save(user);
////
////        int mypoint = 0;
////        int postCount = 1;
////        int commentCount = 5;
////        int lottoCount = 1;
////
////        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
////        pointRepository.save(point);
////
////    }
//
//
//    @Test
//    @Order(1)
//    @DisplayName("?????? ?????? ??????")
//    public void saveUser() {
//        //given
//        User user = new User("123@123", "?????????", "111");
//        userRepository.save(user);
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
//
//    @Test
//    @Order(2)
//    @DisplayName("????????? ??????")
//    void create() {
//        //given
//        PostCreateRequestDto requestDto = new PostCreateRequestDto("??????","??????");
//        User user1 = userRepository.findByUsername("123@123").orElseThrow(
//                ()-> new IllegalArgumentException("???????????? ????????? ???????????? ????????????.")
//        );
//
//        System.out.println(user1.getPoint().getMyPoint());
//
//        //when ????????? ????????????
//        postService.create(requestDto,"",user1);
//
//        //then
//        Post post = postRepository.findByUser(user1).get(0);
//        assertEquals(post.getTitle(),"??????");
//        assertEquals(post.getContent(),"??????");
//        assertEquals(post.getUser().getNickname(),"?????????");
//        assertEquals(post.getUser().getPoint().getMyPoint(),500);
//        assertEquals(post.getUser().getPoint().getPostCount(),0);
//    }
//    @Order(3)
//    @DisplayName("????????? ??????")
//    void delete() {
//        //given
//        User user = userRepository.findByNickname("?????????").get();
//        Post post = postRepository.findByUser(user).get(0);
//        String postUuid = post.getPostUuid();
//
//        //when
//        postService.delete(postUuid, user);
//
//        //then
//        assertFalse(postRepository.findByPostUuid(postUuid).isPresent());
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("?????? ????????? ????????? ??????")
//    void findOneMyPage() {
//        //given
//        PostCreateRequestDto requestDto = new PostCreateRequestDto("??????","??????");
//        PostCreateRequestDto requestDto1 = new PostCreateRequestDto("??????1","??????1");
//        PostCreateRequestDto requestDto2 = new PostCreateRequestDto("??????2","??????2");
//        PostCreateRequestDto requestDto3 = new PostCreateRequestDto("??????3","??????3");
//        User user1 = userRepository.findById(1L).orElseThrow(
//                ()-> new IllegalArgumentException("???????????? ????????? ???????????? ????????????.")
//        );
//        //when ????????? ????????????
//        postService.create(requestDto,"",user1);
//        postService.create(requestDto1,"",user1);
//        postService.create(requestDto2,"",user1);
//        postService.create(requestDto3,"",user1);
//
//        //then
//        List<PostMyPageResponseDto> postMyPageResponseDtos = postService.findOneMyPage(0, user1);
//        //id?????? ???????????? ?????????????????? ????????????.
//        assertEquals(postMyPageResponseDtos.get(0).getTitle(), "??????3");
//        assertEquals(postMyPageResponseDtos.get(1).getTitle(), "??????2");
//        assertEquals(postMyPageResponseDtos.get(2).getTitle(), "??????1");
//        assertEquals(postMyPageResponseDtos.get(3).getTitle(), "??????");
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("????????? 1??? ????????????")
//    void getOnePost() {
//        //given
//        User user = userRepository.findByNickname("?????????").get();
//        Post post = postRepository.findByUser(user).get(0);
//        String postUuid = post.getPostUuid();
//        CommentRequestDto commentRequestDto = new CommentRequestDto();
//        commentRequestDto.setPostUuid(postUuid);
//        commentRequestDto.setContent("??????");
//        commentService.saveComment(commentRequestDto, user, "voiceurl");
//
//        //when
//        PostResponseDto postResponseDto = postService.getOnePost(postUuid, new UserDetailsImpl(user));
//
//        //then
//        assertEquals(postResponseDto.getPostUuid(), postUuid);
//        assertEquals(postResponseDto.getUserId(), 1L);
//        assertEquals(postResponseDto.getTitle(), "??????");
//        assertEquals(postResponseDto.getContent(), "??????");
//        assertEquals(postResponseDto.getNickname(), user.getNickname());
//        assertEquals(postResponseDto.getVoiceUrl(), "");
//        assertEquals(postResponseDto.getComments().get(0).getContent(), "??????");
//        assertEquals(postResponseDto.getComments().get(0).getVoiceUrl(), "voiceurl");
//        assertTrue(postResponseDto.getComments().get(0).isShow()); //????????? ?????????????????? true
//        assertFalse(postResponseDto.getComments().get(0).isLock()); //????????? ???????????? ??????
//        assertNull(postResponseDto.getTimer());
//        assertFalse(postResponseDto.isLock());
////        assertFalse(postResponseDto.isShow());
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("?????? ????????? ??????") //????????? ???????????? ???????????? ???????????? 1?????? ????????? ?????????
//    void findOtherUserPosts() {
//        //given
//        userRepository.save(new User("cbjjzzang@naver.com", "????????????", "1234"));
//        User user = userRepository.findByNickname("????????????").get();
//        int pageId = 0;
//
//        //when
//        List<PostOtherOnePostResponseDto> postOtherOnePostResponseDtos = postService.findOtherUserPosts(user, pageId);
//
//        //then
//        //????????? ????????? ???????????? ??????, ????????? ???????????? ????????????
//        assertEquals(postOtherOnePostResponseDtos.get(3).getUserId(), 1L);
//        assertEquals(postOtherOnePostResponseDtos.get(3).getNickname(), "?????????");
//        assertEquals(postOtherOnePostResponseDtos.get(3).getTitle(), "??????");
//        assertEquals(postOtherOnePostResponseDtos.get(3).getContent(), "??????");
//        assertEquals(postOtherOnePostResponseDtos.get(3).getVoiceUrl(), "");
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("???????????? ????????? ?????? main post ????????????")
//    void getMainPost() {
//        //when
//        MainPostForAnonymousResponseDto mainPostForAnonymousResponseDto = postService.getMainPost();
//
//        //then
//        assertEquals(mainPostForAnonymousResponseDto.getTitle(), "??????");
//        assertEquals(mainPostForAnonymousResponseDto.getContent(), "??????");
//        assertEquals(mainPostForAnonymousResponseDto.getNickname(), "?????????");
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("??? ???????????? ????????? ?????? detail main post ????????????")
//    void getMainDetailPost() {
//        //when
//        PostResponseDto postResponseDto = postService.getMainDetailPost();
//
//        //then
//        assertEquals(postResponseDto.getTitle(), "??????");
//        assertEquals(postResponseDto.getContent(), "??????");
//        assertEquals(postResponseDto.getNickname(), "?????????");
//        assertEquals(postResponseDto.getComments().get(0).getContent(), "??????");
//        assertEquals(postResponseDto.getComments().get(0).getVoiceUrl(), "voiceurl");
//        assertEquals(postResponseDto.getComments().get(0).getNickname(), "?????????");
//        assertEquals(postResponseDto.getComments().get(0).getUserId(), 1L);
//
//
//
//    }
//}