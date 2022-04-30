package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.dto.response.PostMyPageResponseDto;
import com.sparta.bluemoon.dto.response.PostOtherOnePostResponseDto;
import com.sparta.bluemoon.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
        Pageable pageable = PageRequest
            .of(pageId, postSize, Sort.by((Direction.DESC), SORT_PROPERTIES));

        // 내가 쓴 게시글 페이징을 이용해서 들고오기
        Page<Post> pagedPosts = postRepository.findByUser(user, pageable);

        // 들고온 게시글을 dto로 변환해서 반환
        return convertPostsToPostDtos(pagedPosts);
    }

    private List<PostMyPageResponseDto> convertPostsToPostDtos(Page<Post> pagedPosts) {
        List<PostMyPageResponseDto> postDtos = new ArrayList<>();
        for (Post pagedPost : pagedPosts) {
            postDtos.add(new PostMyPageResponseDto(pagedPost));
        }
        return postDtos;
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
}
