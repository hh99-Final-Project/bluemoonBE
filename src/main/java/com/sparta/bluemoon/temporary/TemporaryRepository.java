package com.sparta.bluemoon.temporary;

import com.sparta.bluemoon.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryRepository extends JpaRepository<Temporary, Long> {
    //정렬 추가
    Page<Temporary> findByUser(User user, Pageable pageable);
}
