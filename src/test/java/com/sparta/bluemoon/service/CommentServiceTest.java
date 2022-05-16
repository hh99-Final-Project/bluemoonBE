package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.repository.CommentRepository;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.PostRepository;
import com.sparta.bluemoon.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;


    @Test
    @Order(1)
    public void test() {
        User user = new User("email", "nickname", "password");
        userRepository.save(user);


        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;

        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);

        user = userRepository.findByUsername("email").get();
        PostCreateRequestDto requestDto = new PostCreateRequestDto("제목","내용");
        postService.create(requestDto, "", user);
    }

    @Test
    @Order(2)
    public void save() {
        // given
        User user = userRepository.findByUsername("email").get();
        Post post = postRepository.findByUser(user).get(0);

        // when
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setPostUuid(post.getPostUuid());
        commentRequestDto.setContent("내용");
        commentService.saveComment(commentRequestDto, user, "voiceurl");

        // then
        Comment comment = commentRepository.findAllByPostAndUser(post, user).get(0);
        assertThat(comment.getContent()).isEqualTo("내용");
        assertThat(comment.getUser().getUsername()).isEqualTo("email");
        assertThat(comment.getUser().getNickname()).isEqualTo("nickname");
    }
}
