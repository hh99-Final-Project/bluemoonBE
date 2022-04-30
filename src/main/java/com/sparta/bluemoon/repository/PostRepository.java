package com.sparta.bluemoon.repository;

import com.sparta.bluemoon.domain.Post;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Long countByUserNot(User user);

    List<Post> findByUser(User user);

    Page<Post> findAllByUserNot(User user, Pageable pageable);

    List<Post> findAllByUserNot(User user);

    Page<Post> findByUser(User user, Pageable pageable);
}
