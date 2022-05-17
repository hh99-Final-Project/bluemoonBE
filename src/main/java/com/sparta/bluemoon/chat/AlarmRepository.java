package com.sparta.bluemoon.chat;

import com.sparta.bluemoon.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
   List<Alarm> findByUser(User user);

   Page<Alarm> findByUser(User user, Pageable pageable);
}
