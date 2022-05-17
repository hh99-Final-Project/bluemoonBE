package com.sparta.bluemoon.chatRoom;


import com.sparta.bluemoon.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
   // List<ChatRoomUser> findAllByUser(User user);
    Page<ChatRoomUser> findAllByUser(User user, Pageable pageable);

    void deleteByChatRoomAndUser(ChatRoom chatRoom, User user);

    // ChatRoomUser findByUser(User user);
}
