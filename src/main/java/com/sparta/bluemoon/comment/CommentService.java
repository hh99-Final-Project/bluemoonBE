package com.sparta.bluemoon.comment;

import com.sparta.bluemoon.point.Point;
import com.sparta.bluemoon.post.Post;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.comment.requestDto.CommentRequestDto;
import com.sparta.bluemoon.comment.responseDto.CommentResponseDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.point.PointRepository;
import com.sparta.bluemoon.post.PostRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.sparta.bluemoon.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PointService pointService;
    private final PointRepository pointRepository;

    //댓글 저장
    @Transactional
    public CommentResponseDto saveComment(CommentRequestDto requestDto, User user, String voiceUrl) {

        Post post= postRepository.findByPostUuid(requestDto.getPostUuid()).orElseThrow(
                () -> new CustomException(DOESNT_EXIST_POST_FOR_WRITE)
        );

        // 상위 댓글 정보 추출
        Comment parentComment = commentRepository
            .findByCommentUuid(requestDto.getParentUuid()).orElse(null);

        Comment comment = new Comment(requestDto, user, post, voiceUrl, parentComment);
        commentRepository.save(comment);

        //댓글 작성시간
        String dateResult = getCurrentTime();

        //유저의 현재 포인트
        int userPoint = user.getPoint().getMyPoint();

        //게시글에 유저가 쓴 코멘트가 존재하는지 판단
        List<Comment> userComments = commentRepository.findAllByPostAndUser(post, user);

        Point point = user.getPoint();

        //처음썼고 카운트가 남아있다면 포인트 주기 있다면 넘어가기
        if((userComments.size() == 1) && (point.getCommentCount() != 0)){
            userPoint = pointService.pointChange(point,"COMMENT_POINT");
        }

        return new CommentResponseDto(requestDto, user, dateResult, userPoint);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(String commentUuid, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentUuid(commentUuid).orElseThrow(
            () -> new CustomException(DOESNT_EXIST_POST_FOR_DELETE)
        );

        if (!comment.getUser().getId().equals(userDetails.getUser().getId()) ||
                !comment.getPost().getUser().getId().equals(userDetails.getUser().getId())){
            throw new CustomException(ONLY_CAN_DELETE_COMMENT_WRITER);
        }

        if(comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
            comment.changeDeletedStatus(DeleteStatus.Y);
        } else { // 삭제 가능한 조상 댓글을 구해서 삭제
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
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
