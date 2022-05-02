package com.sparta.bluemoon.repository;

import com.sparta.bluemoon.domain.Temporary;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemporaryRepository extends JpaRepository<Temporary, Long> {
    //정렬 추가
    List<Temporary> findByUser(User user);
}