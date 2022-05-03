package com.sparta.bluemoon.repository;


import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostAndUser(Post post, User user);
}
