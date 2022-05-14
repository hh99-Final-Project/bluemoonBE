package com.sparta.bluemoon.repository;

import com.sparta.bluemoon.domain.Alarm;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
   List<Alarm> findByUser(User user);

   Page<Alarm> findByUser(User user, Pageable pageable);
}
