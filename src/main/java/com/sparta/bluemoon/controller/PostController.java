package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.request.PostCreateRequestDto;
import com.sparta.bluemoon.dto.response.*;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.PostService;
import java.io.IOException;
import java.util.List;
import com.sparta.bluemoon.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final VoiceService voiceService;

    //게시글 작성
    @PostMapping(value = "/api/posts", consumes = {"multipart/form-data"})
    public PostCreateResponseDto create(@RequestPart(required = false) PostCreateRequestDto requestDto,
                         @RequestPart(required = false) MultipartFile file,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String voiceUrl = "";
        if (file!=null) {
            voiceUrl = voiceService.upload(file, "static");
        }
        return postService.create(requestDto, voiceUrl, userDetails.getUser());

    }

    // 나의 게시글 전체 조회 (페이지당 5건, id를 기준으로 내림차순으로 반환)
    @GetMapping("/api/myposts/{pageId}")
    public List<PostMyPageResponseDto> getMyPost(@PathVariable Integer pageId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pageId -= 1;
        return postService.findOneMyPage(pageId, userDetails.getUser());
    }

    // 남의 게시글 5개 조회
    // toDo: 코드 리팩토링 하기전
    @GetMapping("/api/posts/{pageId}")
    public List<PostOtherOnePostResponseDto> getOtherPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable int pageId) {
        pageId -= 1;
        return postService.findOtherUserPosts(userDetails.getUser(), pageId);
    }

    //게시글 1개 상세 조회
    @GetMapping("/api/postsDetail/{postId}")
    public PostResponseDto getOnePost(@PathVariable String postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getOnePost(postId, userDetails);
    }

    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public void delete(@PathVariable String postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.delete(postId, userDetails.getUser());
    }

    // 비로그인한 사용자에게 보여줄 게시글
    @GetMapping("/api/posts/anonymous")
    public MainPostForAnonymousResponseDto getMainPost() {
        return postService.getMainPost();
    }

    // 비로그인한 사용자에게 보여줄 게시글 상세페이지
    @GetMapping("/api/posts/anonymous/one")
    public PostResponseDto getMainDetailPost() {
        return postService.getMainDetailPost();
    }
}
