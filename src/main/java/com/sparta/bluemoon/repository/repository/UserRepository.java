package com.sparta.bluemoon.repository.repository;


import com.sparta.bluemoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);

    Optional<User> findByUsername(String email);
    

}
