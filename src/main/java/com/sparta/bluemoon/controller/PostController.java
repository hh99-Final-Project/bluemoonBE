package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.dto.response.PostMyPageResponseDto;
import com.sparta.bluemoon.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 저장
    @PostMapping("/api/posts")
    public void create(@RequestBody PostCreateRequestDto postCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.create(postCreateRequestDto, userDetails.getUser());
    }

    // 나의 게시글 1개 조회 (페이지당 한건, id를 기준으로 내림차순으로 반환)
    @GetMapping("/api/myposts/{pageId}")
    public List<PostMyPageResponseDto> getMyPost(@PathVariable Integer pageId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pageId -= 1;
        return postService.findOneMyPage(pageId, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.delete(postId, userDetails.getUser());
    }
}
