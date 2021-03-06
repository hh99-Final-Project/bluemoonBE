package com.sparta.bluemoon.comment;


import com.sparta.bluemoon.post.Post;
import com.sparta.bluemoon.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByPostAndUser(Post post, User user);

    Optional<Comment> findByCommentUuid(String commentId);
}
