package com.sparta.bluemoon.repository;

import com.sparta.bluemoon.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
