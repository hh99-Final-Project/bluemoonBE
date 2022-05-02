package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.CommentDto;
import com.sparta.bluemoon.dto.CommentListDto;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.dto.response.MainPostForAnonymousResponseDto;
import com.sparta.bluemoon.dto.response.PostMyPageResponseDto;
import com.sparta.bluemoon.dto.response.PostOtherOnePostResponseDto;
import com.sparta.bluemoon.dto.response.PostResponseDto;
import com.sparta.bluemoon.dto.response.SocialLoginResponseDto;
import com.sparta.bluemoon.repository.CommentRepository;
import com.sparta.bluemoon.repository.PostRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    // 내 게시글 한 페이지당 보여줄 게시글의 수
    private static final int MY_POST_PAGEABLE_SIZE = 5;
    // 페이지 sort 대상 (id를 기준으로 내림차순으로 sort할 에정임)
    private static final String SORT_PROPERTIES = "id";
    // 남의 게시글 한 페이지당 보여줄 게시글의 수 (한 페이지당 보여줄 게시글의 수는 1개이지만 5개를 한번에 보내주기로 함)
    private static final int OTHER_POST_PAGEABLE_SIZE = 1;
    // 비로그인 사용자에게 보여줄 main post index
    private static final long MAIN_POST_INDEX_FOR_ANONYMOUS = 1L;

    //게시글 1개 상세 조회
    public PostResponseDto getPost(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
        );
        // 댓글의 삭제 가능 여부를 확인한 뒤 Dto로 변환
        List<CommentDto> newCommentList = getCommentDtos(userDetails, post);

        //Dto에 담아주기
        CommentListDto commentListDto = new CommentListDto(newCommentList);

        return new PostResponseDto(post, commentListDto);
    }

    // 게시글(다이어리) 저장
    public void create(PostCreateRequestDto postCreateRequestDto, User user) {
        Post post = new Post(postCreateRequestDto, user);
        postRepository.save(post);
    }

    // 게시글(다이어리) 삭제
    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        if (!user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("게시글 작성자 만이 게시글을 삭제할 수 있습니다.");
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


    // 남의 게시글 훔쳐보기 (1개만)
    public PostOtherOnePostResponseDto findOneOtherPage(User user) {

        long otherPostsCount = postRepository.countByUserNot(user);

        if (otherPostsCount < 1) {
            throw new IllegalArgumentException("남이 쓴 게시글이 존재하지 않습니다.");
        }

        List<Post> otherPosts = postRepository.findAllByUserNot(user);

        int idx = (int)(Math.random() * otherPosts.size());
        Post post = otherPosts.get(idx);
        return new PostOtherOnePostResponseDto(post);
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
        User user = post.getUser();

        List<CommentDto> newComments = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByPost(post);
        for (Comment comment: comments) {
            if (comment.getUser().getId().equals(userDetails.getUser().getId()) || userDetails.getUser().getId().equals(user.getId())) {
                comment.setShow(true);
            }
            CommentDto commentDto = new CommentDto(comment);
            newComments.add(commentDto);
        }
        return newComments;
    }

    // 비 로그인한 유저를 위해 main post를 보여주기
    public MainPostForAnonymousResponseDto getMainPost() {
        Post post = postRepository.findById(MAIN_POST_INDEX_FOR_ANONYMOUS).orElseThrow(
            () -> new IllegalArgumentException("main 게시글이 존재하지 않습니다.")
        );

        return new MainPostForAnonymousResponseDto(post);
    }

    // 비 로그인한 유저를 위해 detail main post를 보여주기
    public PostResponseDto getMainDetailPost() {
        Post post = postRepository.findById(MAIN_POST_INDEX_FOR_ANONYMOUS).orElseThrow(
            () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
        );

        // 댓글의 삭제 가능 여부를 확인한 뒤 Dto로 변환
        List<Comment> comments = commentRepository.findAllByPost(post);
        List<CommentDto> newComments = new ArrayList<>();
        for (Comment comment: comments) {
            comment.setShow(false);
            CommentDto commentDto = new CommentDto(comment);
            newComments.add(commentDto);
        }

        //Dto에 담아주기
        CommentListDto commentListDto = new CommentListDto(newComments);

        return new PostResponseDto(post, commentListDto);
    }
}
