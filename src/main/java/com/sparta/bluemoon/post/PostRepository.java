package com.sparta.bluemoon.post;

import com.sparta.bluemoon.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

   // int countByUserNot(User user);

    List<Post> findByUser(User user);

//    Page<Post> findAllByUserNot(User user, Pageable pageable);
//
//    List<Post> findAllByUserNot(User user);
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByUser(User user, Pageable pageable);

    Optional<Post> findByPostUuid(String postId);
}
