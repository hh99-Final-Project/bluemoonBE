package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.dto.request.CommentRequestDto;
import com.sparta.bluemoon.dto.response.CommentResponseDto;
import com.sparta.bluemoon.repository.CommentRepository;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.PostRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PointService pointService;
    private final PointRepository pointRepository;




    //댓글 저장
    public CommentResponseDto saveComment(CommentRequestDto requestDto, UserDetailsImpl userDetails, String voiceUrl) {

        Post post= postRepository.findByPostUuid(requestDto.getPostUuid()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment(requestDto, userDetails, post, voiceUrl);
        commentRepository.save(comment);

        //댓글 작성시간
        String dateResult = getCurrentTime();

        //유저의 현재 포인트
        int userPoint = userDetails.getUser().getPoint().getMyPoint();
        //게시글에 유저가 쓴 코멘트가 존재하는지 판단
        List<Comment> userComments = commentRepository.findAllByPostAndUser(post,userDetails.getUser());
        //처음썼고 카운트가 남아있다면 포인트 주기 있다면 넘어가기
        Point point = pointRepository.findByUser(userDetails.getUser());

        if((userComments.size()==1)&&(point.getCommentCount()!=0)){
             userPoint = pointService.pointChange(point,"COMMENT_POINT");
        }

        return new CommentResponseDto(requestDto, userDetails, dateResult, userPoint);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(String commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentUuid(commentId).orElseThrow(
                ()-> new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다.")
        );
        if (!comment.getUser().getId().equals(userDetails.getUser().getId())){
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
