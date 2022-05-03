package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.response.CommentResponseDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.CommentService;
import com.sparta.bluemoon.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final VoiceService voiceService;

    //댓글 저장
    @PostMapping(value = "/api/comments", consumes = {"multipart/form-data"})
    public CommentResponseDto saveComment(@RequestPart(required = false) CommentRequestDto requestDto,
                                          @RequestPart(required = false) MultipartFile file,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String voiceUrl = "";
        if(file != null){
            voiceUrl = voiceService.upload(file,"static");
        }
        return commentService.saveComment(requestDto, userDetails, voiceUrl);
    }
    //댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public void deleteComment(@PathVariable String commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId, userDetails);
    }
}
