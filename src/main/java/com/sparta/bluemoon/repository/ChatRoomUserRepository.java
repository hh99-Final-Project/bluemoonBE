package com.sparta.bluemoon.repository;


import com.sparta.bluemoon.domain.ChatRoomUser;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
    List<ChatRoomUser> findAllByUser(User user);

    ChatRoomUser findByUser(User user);
}
