package com.sparta.bluemoon.point;


import com.sparta.bluemoon.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointRepository extends JpaRepository<Point, Long> {

    Point findByUser(User user);
}
