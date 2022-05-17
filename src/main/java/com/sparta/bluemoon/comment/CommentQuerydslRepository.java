package com.sparta.bluemoon.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bluemoon.post.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sparta.bluemoon.comment.QComment.comment;

@Repository
public class CommentQuerydslRepository {

    // jpa 접근할 때 필요함
    private final EntityManager em;
    // querydsl 사용시 필요함
    private final JPAQueryFactory queryFactory;

    public CommentQuerydslRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Comment> findCommentByPost(Post post) {
        // 게시글에 해당하는 모든 댓글을 들고오고
        // List<CommentDto>

        return queryFactory.selectFrom(comment)
            .leftJoin(comment.parent)
            .fetchJoin()
            .where(comment.post.eq(post))
            .orderBy(
                comment.parent.id.asc().nullsFirst(),
                comment.createdAt.asc()
            ).fetch();
    }
}
