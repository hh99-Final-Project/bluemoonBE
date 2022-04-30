package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.delete(postId, userDetails.getUser());
    }
}
