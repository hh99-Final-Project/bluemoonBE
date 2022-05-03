package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.response.CommentResponseDto;
import com.sparta.bluemoon.repository.CommentRepository;
import com.sparta.bluemoon.repository.PostRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //댓글 저장
    public CommentResponseDto saveComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Post post= postRepository.findByPostUuid(commentRequestDto.getPostUuid()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment(commentRequestDto, userDetails, post);
        commentRepository.save(comment);

        //댓글 작성시간
        String dateResult = getCurrentTime();

        return new CommentResponseDto(commentRequestDto, userDetails, dateResult);

    }

    //댓글 삭제
    public void deleteComment(String commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentUuid(commentId).orElseThrow(
                ()-> new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다.")
        );
        if (!comment.getUser().equals(userDetails.getUser())){
            throw new IllegalArgumentException("글을 작성한 유저만 삭제할 수 있습니다.");
        }
        commentRepository.deleteByCommentUuid(commentId);
    }

    //현재시간 추출 메소드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }
}
