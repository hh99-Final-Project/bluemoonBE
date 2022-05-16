package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.dto.response.MainPostForAnonymousResponseDto;
import com.sparta.bluemoon.dto.response.PostMyPageResponseDto;
import com.sparta.bluemoon.dto.response.PostOtherOnePostResponseDto;
import com.sparta.bluemoon.dto.response.PostResponseDto;
import com.sparta.bluemoon.repository.*;
import com.sparta.bluemoon.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

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


//    @Test
////    @BeforeEach
//    @DisplayName("유저 정보 저장")
//    public void saveUser() {
//
//        User user = new User("123@123", "김승민", "111");
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

    @Test
    @Order(1)
    @DisplayName("포스트 생성")
    void create() {
        //given
        User user = new User("123@123", "김승민", "111");
        userRepository.save(user);

        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;

        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);

        PostCreateRequestDto requestDto = new PostCreateRequestDto("제목","내용");
        User user1 = userRepository.findByUsername("123@123").orElseThrow(
                ()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.")
        );

        System.out.println(user1.getPoint().getMyPoint());
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
    @Order(2)
    @DisplayName("포스트 삭제")
    void delete() {
        //given
        User user = userRepository.findByNickname("김승민").get();
        Post post = postRepository.findByUser(user).get(0);
        String postUuid = post.getPostUuid();

        //when
        postService.delete(postUuid, user);

        //then
        assertFalse(postRepository.findByPostUuid(postUuid).isPresent());
    }

    @Test
    @Order(3)
    @DisplayName("나의 게시글 리스트 조회")
    void findOneMyPage() {
        //given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("제목","내용");
        PostCreateRequestDto requestDto1 = new PostCreateRequestDto("제목1","내용1");
        PostCreateRequestDto requestDto2 = new PostCreateRequestDto("제목2","내용2");
        PostCreateRequestDto requestDto3 = new PostCreateRequestDto("제목3","내용3");
        User user1 = userRepository.findById(1L).orElseThrow(
                ()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.")
        );
        //when 게시글 작성하면
        postService.create(requestDto,"",user1);
        postService.create(requestDto1,"",user1);
        postService.create(requestDto2,"",user1);
        postService.create(requestDto3,"",user1);

        //then
        List<PostMyPageResponseDto> postMyPageResponseDtos = postService.findOneMyPage(0, user1);
        //id값을 기준으로 내림차순으로 정렬한다.
        assertEquals(postMyPageResponseDtos.get(0).getTitle(), "제목3");
        assertEquals(postMyPageResponseDtos.get(1).getTitle(), "제목2");
        assertEquals(postMyPageResponseDtos.get(2).getTitle(), "제목1");
        assertEquals(postMyPageResponseDtos.get(3).getTitle(), "제목");
    }

    @Test
    @Order(4)
    @DisplayName("게시글 1개 상세조회")
    void getOnePost() {
        //given
        User user = userRepository.findByNickname("김승민").get();
        Post post = postRepository.findByUser(user).get(0);
        String postUuid = post.getPostUuid();
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setPostUuid(postUuid);
        commentRequestDto.setContent("내용");
        commentService.saveComment(commentRequestDto, user, "voiceurl");

        //when
        PostResponseDto postResponseDto = postService.getOnePost(postUuid, new UserDetailsImpl(user));

        //then
        assertEquals(postResponseDto.getPostUuid(), postUuid);
        assertEquals(postResponseDto.getUserId(), 1L);
        assertEquals(postResponseDto.getTitle(), "제목");
        assertEquals(postResponseDto.getContent(), "내용");
        assertEquals(postResponseDto.getNickname(), user.getNickname());
        assertEquals(postResponseDto.getVoiceUrl(), "");
        assertEquals(postResponseDto.getComments().get(0).getContent(), "내용");
        assertEquals(postResponseDto.getComments().get(0).getVoiceUrl(), "voiceurl");
        assertTrue(postResponseDto.getComments().get(0).isShow()); //본인이 확인했음으로 true
        assertFalse(postResponseDto.getComments().get(0).isLock()); //잠금을 걸었는지 확인
        assertNull(postResponseDto.getTimer());
        assertFalse(postResponseDto.isLock());
//        assertFalse(postResponseDto.isShow());
    }

    @Test
    @Order(5)
    @DisplayName("남의 게시글 보기") //페이징 형식으로 리스트로 조회해서 1개씩 화면에 보여줌
    void findOtherUserPosts() {
        //given
        userRepository.save(new User("cbjjzzang@naver.com", "가나다라", "1234"));
        User user = userRepository.findByNickname("가나다라").get();
        int pageId = 0;

        //when
        List<PostOtherOnePostResponseDto> postOtherOnePostResponseDtos = postService.findOtherUserPosts(user, pageId);

        //then
        //리스트 형태로 내림차순 정렬, 마지막 인덱스를 검사한다
        assertEquals(postOtherOnePostResponseDtos.get(3).getUserId(), 1L);
        assertEquals(postOtherOnePostResponseDtos.get(3).getNickname(), "김승민");
        assertEquals(postOtherOnePostResponseDtos.get(3).getTitle(), "제목");
        assertEquals(postOtherOnePostResponseDtos.get(3).getContent(), "내용");
        assertEquals(postOtherOnePostResponseDtos.get(3).getVoiceUrl(), "");
    }

    @Test
    @Order(6)
    @DisplayName("비로그인 유저를 위한 main post 보여주기")
    void getMainPost() {
        //when
        MainPostForAnonymousResponseDto mainPostForAnonymousResponseDto = postService.getMainPost();

        //then
        assertEquals(mainPostForAnonymousResponseDto.getTitle(), "제목");
        assertEquals(mainPostForAnonymousResponseDto.getContent(), "내용");
        assertEquals(mainPostForAnonymousResponseDto.getNickname(), "김승민");
    }

    @Test
    @Order(7)
    @DisplayName("비 로그인한 유저를 위해 detail main post 보여주기")
    void getMainDetailPost() {
        //when
        PostResponseDto postResponseDto = postService.getMainDetailPost();

        //then
        assertEquals(postResponseDto.getTitle(), "제목");
        assertEquals(postResponseDto.getContent(), "내용");
        assertEquals(postResponseDto.getNickname(), "김승민");
        assertEquals(postResponseDto.getComments().get(0).getContent(), "내용");
        assertEquals(postResponseDto.getComments().get(0).getVoiceUrl(), "voiceurl");
        assertEquals(postResponseDto.getComments().get(0).getNickname(), "김승민");
        assertEquals(postResponseDto.getComments().get(0).getUserId(), 1L);

    }
}