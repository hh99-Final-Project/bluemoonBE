package com.sparta.bluemoon.post;

import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.post.reponseDto.*;
import com.sparta.bluemoon.post.requestDto.PostCreateRequestDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.user.UserRoleEnum;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.user.UserRepository;
import com.sparta.bluemoon.util.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final VoiceService voiceService;

    //게시글 작성
    @PostMapping(value = "/api/posts", consumes = {"multipart/form-data"})
    public PostCreateResponseDto create(@Valid @RequestPart(required = false) PostCreateRequestDto requestDto,
                                        @RequestPart(required = false) MultipartFile file,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String voiceUrl = "";
        if (file!=null) {
            voiceUrl = voiceService.upload(file, "static");
        }
        return postService.create(requestDto, voiceUrl, userDetails.getUser());

    }

    // 나의 게시글 전체 조회 (페이지당 10건, id를 기준으로 내림차순으로 반환)
    @GetMapping("/api/myposts/{pageId}")
    public List<PostMyPageResponseDto> getMyPost(@PathVariable Integer pageId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pageId -= 1;
        if (userDetails.getUser().getRole() != null && userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return postService.findAdminPage(pageId, userDetails.getUser());
        }
        return postService.findOneMyPage(pageId, userDetails.getUser());
    }

    // 남의 게시글 5개 조회
    @GetMapping("/api/posts/{pageId}")
    public List<AllPostResponseDto> getAllPost(@PathVariable int pageId) {
        pageId -= 1;

        return postService.findOtherUserPosts(pageId);
    }

    //게시글 1개 상세 조회
    @GetMapping("/api/postsDetail/{postId}")
    public PostResponseDto getOnePost(@PathVariable String postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole() != null && userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            return postService.getAdminPost(postId, userDetails);
        }
        return postService.getOnePost(postId, userDetails);
    }

    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public void delete(@PathVariable String postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole() != null && userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN)) {
            postService.adminDelete(postId, userDetails.getUser());
        }
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
