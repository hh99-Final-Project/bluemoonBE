package com.sparta.bluemoon.user;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsernameAndType(String email, String type);

    Optional<User> findByUsername(String username);
}
