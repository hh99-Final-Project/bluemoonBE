package com.sparta.bluemoon.repository;

import com.sparta.bluemoon.domain.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findByNicknameAndPersonalInfo(String nickname, boolean personalInfo);
}
