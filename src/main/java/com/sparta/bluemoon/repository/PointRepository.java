package com.sparta.bluemoon.repository;


import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByUser(User user);
}
