package com.sparta.bluemoon.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bluemoon.domain.Comment;
import com.sparta.bluemoon.domain.Post;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Optional<Comment> findByCommentUuid(String commentId);

    void deleteByCommentUuid(String commentId);
}
