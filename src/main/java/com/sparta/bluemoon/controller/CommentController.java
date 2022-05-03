package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.response.CommentResponseDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 저장
    @PostMapping("/api/comments")
    public CommentResponseDto saveComment(@RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.saveComment(commentRequestDto, userDetails);
    }
    //댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public void deleteComment(@PathVariable String commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId, userDetails);
    }
}
