package com.sparta.bluemoon.repository;


import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.domain.ChatRoomUser;
import com.sparta.bluemoon.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
   // List<ChatRoomUser> findAllByUser(User user);
    Page<ChatRoomUser> findAllByUser(User user, Pageable pageable);

    void deleteByChatRoomAndUser(ChatRoom chatRoom, User user);

    // ChatRoomUser findByUser(User user);
}
