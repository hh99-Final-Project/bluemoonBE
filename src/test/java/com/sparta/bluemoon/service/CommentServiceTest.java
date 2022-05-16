package com.sparta.bluemoon.service;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.QComment;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.dto.response.CommentResponseDto;
import com.sparta.bluemoon.repository.CommentQuerydslRepository;

import com.sparta.bluemoon.repository.CommentRepository;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.PostRepository;
import com.sparta.bluemoon.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.bluemoon.domain.QComment.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentServiceTest {

    @PersistenceContext
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

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
    @DisplayName("사용자 정보 저장, 게시글 저장")
    public void preTest() {
        List<User> all = userRepository.findAll();
        User user = new User("username", "nickname", "password");
        userRepository.save(user);

        int mypoint = 0;
        int postCount = 1;
        int commentCount = 5;
        int lottoCount = 1;

        Point point = new Point(mypoint, user, postCount, commentCount, lottoCount);
        pointRepository.save(point);

        user = userRepository.findByUsername("username").get();

        PostCreateRequestDto requestDto = new PostCreateRequestDto("제목","내용");
        postService.create(requestDto, "", user);
    }

    @Test
    @Order(2)

    @DisplayName("댓글 저장")
    public void save1() {
        // given
        User user = userRepository.findByUsername("username").get();
        Post post = postRepository.findByUser(user).get(0);


        // when
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setPostUuid(post.getPostUuid());
        commentRequestDto.setContent("내용");
        commentService.saveComment(commentRequestDto, user, "voiceurl");

        // then
        Comment comment = commentRepository.findAllByPostAndUser(post, user).get(0);
        assertThat(comment.getContent()).isEqualTo("내용");

        assertThat(comment.getUser().getUsername()).isEqualTo("username");
        assertThat(comment.getUser().getNickname()).isEqualTo("nickname");
    }

    @Test
    @Order(3)
    @DisplayName("대댓글 저장")
    public void save2() {
        // given
        User user = userRepository.findByUsername("username").get();
        Post post = postRepository.findByUser(user).get(0);
        Post post1 = postRepository.findByPostUuid(post.getPostUuid()).get();
        System.out.println(post1.getPostUuid());
        System.out.println("post.getPostUuid() = " + post.getPostUuid());
        Comment parentComment = commentRepository.findAllByPost(post).get(0);

        System.out.println(commentRepository.findAll().size());

        // when
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setPostUuid(post.getPostUuid());
        commentRequestDto.setContent("대댓글1");
        commentRequestDto.setParentUuid(parentComment.getCommentUuid());
        commentService
            .saveComment(commentRequestDto, user, "voiceurl");

        // then
        Comment childComment = commentRepository.findByContent("대댓글1").get(0);
        assertThat(childComment.getParent().getId()).isEqualTo(parentComment.getId());
    }

}
