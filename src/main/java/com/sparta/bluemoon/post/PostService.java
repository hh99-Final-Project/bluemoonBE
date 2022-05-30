package com.sparta.bluemoon.post;

import com.sparta.bluemoon.comment.Comment;
import com.sparta.bluemoon.point.Point;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.comment.responseDto.CommentDto;
import com.sparta.bluemoon.post.reponseDto.*;
import com.sparta.bluemoon.post.requestDto.PostCreateRequestDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.comment.CommentQuerydslRepository;
import com.sparta.bluemoon.comment.CommentRepository;
import com.sparta.bluemoon.point.PointRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.user.UserRoleEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sparta.bluemoon.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.sparta.bluemoon.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PointRepository pointRepository;
    private final CommentRepository commentRepository;
    private final CommentQuerydslRepository commentQuerydslRepository;
    private final PointService pointService;
    private final EntityManager em;

    // 내 게시글 한 페이지당 보여줄 게시글의 수
    private static final int MY_POST_PAGEABLE_SIZE = 10;
    // 페이지 sort 대상 (id를 기준으로 내림차순으로 sort할 에정임)
    private static final String SORT_PROPERTIES = "id";
    // 남의 게시글 한 페이지당 보여줄 게시글의 수 (한 페이지당 보여줄 게시글의 수는 1개이지만 5개를 한번에 보내주기로 함)
    private static final int POST_PAGEABLE_SIZE = 5;

    //게시글 1개 상세 조회
    public PostResponseDto getOnePost(String postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findByPostUuid(postId).orElseThrow(
            () -> new CustomException(CANNOT_FIND_POST_NOT_EXIST)
        );

        // 댓글의 삭제 가능 여부를 확인한 뒤 Dto로 변환
        List<CommentDto> newCommentList = getCommentDtos(userDetails, post);
        //댓글 비공개 시 볼 수 있는 사람 특정

        return new PostResponseDto(post, newCommentList);
    }

    // 게시글(다이어리) 저장
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto postCreateRequestDto, String voiceUrl, User user) {
        Post post = new Post(postCreateRequestDto, voiceUrl, user);
        postRepository.save(post);

        int userPoint = user.getPoint().getMyPoint();
        if (user.getPoint().getPostCount() != 0) {
            userPoint = pointService.pointChange(user.getPoint(), "POST_POINT");
        }
        //TODO: 임의로 음성녹음 파일 추가
        return new PostCreateResponseDto(voiceUrl, userPoint);
    }

    public void createWithoutVoice(PostCreateRequestDto postCreateRequestDto, User user) {
        Post post = new Post(postCreateRequestDto, user);
        postRepository.save(post);
    }

    // 게시글(다이어리) 삭제
    public void delete(String postId, User user) {
        Post post = postRepository.findByPostUuid(postId).orElseThrow(
                () -> new CustomException(CANNOT_DELETE_NOT_EXIST_POST)
        );

        if (!user.getId().equals(post.getUser().getId())) {
            throw new CustomException(ONLY_CAN_DELETE_POST_WRITER);
        }

        postRepository.delete(post);
    }

    // 나의 게시글 리스트 조회 (마이 페이지)
    public List<PostMyPageResponseDto> findOneMyPage(Integer pageId, User user) {

        // paging 처리 해야 하는 수 보다 게시글의 수가 적을 경우 고려
        int postSize = Math.min(postRepository.findByUser(user).size(), MY_POST_PAGEABLE_SIZE);
        try {
            Pageable pageable = PageRequest
                .of(pageId, postSize, Sort.by((Direction.DESC), SORT_PROPERTIES));
            // 내가 쓴 게시글 페이징을 이용해서 들고오기
            Page<Post> pagedPosts = postRepository.findByUser(user, pageable);
            // 들고온 게시글을 dto로 변환해서 반환
            return convertPostsToPostDtos(pagedPosts);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    // 남의 게시글 훔쳐보기 (5개만)
    public List<AllPostResponseDto> findOtherUserPosts(int pageId) {
        // 전체 게시글 수
        int postsCount = postRepository.findAll().size();

        // paging 처리 해야 하는 수 보다 게시글의 수가 적을 경우 고려
        int postSize = Math.min(postsCount,POST_PAGEABLE_SIZE);
        try {
            Pageable pageable = PageRequest
                .of(pageId, postSize, Sort.by((Direction.DESC), SORT_PROPERTIES));
            Page<Post> posts = postRepository.findAll(pageable);

            List<AllPostResponseDto> postDtos = new ArrayList<>();
            for (Post post : posts.getContent()) {
                postDtos.add(new AllPostResponseDto(post));
            }

            return postDtos;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    //나의 게시글 리스트 조회 및 댓글 개수 추가
    private List<PostMyPageResponseDto> convertPostsToPostDtos(Page<Post> pagedPosts) {
        List<PostMyPageResponseDto> postDtos = new ArrayList<>();
        for (Post pagedPost : pagedPosts) {
            List<Comment> comments = commentRepository.findAllByPost(pagedPost);
            int count = comments.size();
            postDtos.add(new PostMyPageResponseDto(pagedPost, count));
        }
        return postDtos;
    }

    //댓글에 로그인한 유저와 비교해 삭제 가능 여부 판단해주는 메소드
    private List<CommentDto> getCommentDtos(UserDetailsImpl userDetails, Post post) {
        List<CommentDto> result = new ArrayList<>();
        Map<String, CommentDto> commentMap = new HashMap<>();

        // 게시글에 해당하는 댓글 들고오기
        List<Comment> comments = commentQuerydslRepository.findCommentByPost(post);

        comments.forEach(comment -> {
            CommentDto commentDto = new CommentDto(comment);
            // 내가 댓글을 작성한 사람이거나, 내가 게시글을 작성한 사람일 경우 setShow -> true
            if (userDetails != null && ((userDetails.getUser().getRole() != null && userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) || comment.getUser().getId().equals(userDetails.getUser().getId()) || userDetails.getUser().getId().equals(post.getUser().getId()))) {
                commentDto.setShow(true);
            }

            commentMap.put(commentDto.getCommentUuid(), commentDto);
            // 부모 댓글이 존재하는 경우
            if(comment.getParent() != null) {
                // 부모 댓글에 자식 댓글 추가
                commentMap.get(comment.getParent().getCommentUuid()).getChildren().add(commentDto);
            } else {
                result.add(commentDto);
            }
        });

        Collections.reverse(result);
        return result;
    }

    // 비 로그인한 유저를 위해 main post를 보여주기
    public MainPostForAnonymousResponseDto getMainPost() {
        Post post = postRepository.findAll().get(0);

        return new MainPostForAnonymousResponseDto(post);
    }

    // 비 로그인한 유저를 위해 detail main post를 보여주기
    public PostResponseDto getMainDetailPost() {
        Post post = postRepository.findAll().get(0);

        // 댓글의 삭제 가능 여부를 확인한 뒤 Dto로 변환
        List<CommentDto> newComments = getCommentDtos(null, post);

        //Dto에 담아주기
        return new PostResponseDto(post, newComments);
    }

    public List<PostMyPageResponseDto> findAdminPage(Integer pageId, User user) {

        try {
            Pageable pageable = PageRequest
                .of(pageId, MY_POST_PAGEABLE_SIZE, Sort.by((Direction.DESC), SORT_PROPERTIES));
            // 내가 쓴 게시글 페이징을 이용해서 들고오기
            Page<Post> pagedPosts = postRepository.findAll(pageable);
            // 들고온 게시글을 dto로 변환해서 반환
            return convertPostsToPostDtos(pagedPosts);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public PostResponseDto getAdminPost(String postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findByPostUuid(postId).orElseThrow(
            () -> new CustomException(CANNOT_FIND_POST_NOT_EXIST)
        );

        // 댓글의 삭제 가능 여부를 확인한 뒤 Dto로 변환
        List<CommentDto> newCommentList = getCommentDtos(userDetails, post);
        //댓글 비공개 시 볼 수 있는 사람 특정

        return new PostResponseDto(post, newCommentList);
    }

    public void adminDelete(String postId, User user) {
        Post post = postRepository.findByPostUuid(postId).orElseThrow(
            () -> new CustomException(CANNOT_DELETE_NOT_EXIST_POST)
        );

//        if (!user.getId().equals(post.getUser().getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
//            throw new CustomException(ONLY_CAN_DELETE_POST_WRITER);
//        }

        postRepository.delete(post);
    }
}
