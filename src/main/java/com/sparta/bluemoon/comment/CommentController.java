package com.sparta.bluemoon.comment;

import com.sparta.bluemoon.comment.requestDto.CommentRequestDto;
import com.sparta.bluemoon.comment.responseDto.CommentResponseDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.util.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final VoiceService voiceService;

    // 댓글 저장
    // 댓글 저장할떄는 상위 댓글의 정보를 받아와야한다.
    // 최상위 댓글의 경우 상위 댓글이 존재하지 않으므로 값을 빈칸으로 보내주면 된다.
    @PostMapping(value = "/api/comments", consumes = {"multipart/form-data"})
    public CommentResponseDto saveComment(@Valid @RequestPart(required = false) CommentRequestDto requestDto,
                                          @RequestPart(required = false) MultipartFile file,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String voiceUrl = "";
        if(file != null){
            voiceUrl = voiceService.upload(file,"static");
        }
        return commentService.saveComment(requestDto, userDetails.getUser(), voiceUrl);
    }

    //댓글 삭제
    @DeleteMapping("/api/comments/{commentUuid}")
    public void deleteComment(@PathVariable String commentUuid, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentUuid, userDetails);
    }
}
