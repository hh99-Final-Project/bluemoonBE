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
//    @PostMapping("/api/comments")
//    public CommentResponseDto saveComment(@RequestBody CommentRequestDto commentRequestDto,
//        @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.saveComment(commentRequestDto, userDetails);
//    }


    // 댓글 저장할떄는 상위 댓글의 정보를 받아와야한다.
    // 최상위 댓글의 경우 상위 댓글이 존재하지 않으므로 값을 빈칸으로 보내주면 된다.
    @PostMapping("/api/comments")
    public CommentResponseDto saveComment(
        @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.saveComment(commentRequestDto, userDetails);
    }

    //댓글 삭제
    @DeleteMapping("/api/comments/{commentUuid}")
    public void deleteComment(@PathVariable String commentUuid, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentUuid, userDetails);
    }
}
