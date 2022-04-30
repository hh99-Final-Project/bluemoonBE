package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import lombok.RequiredArgsConstructor;
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
}
