package com.sparta.bluemoon.repository;

import com.sparta.bluemoon.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

}
