package com.sparta.bluemoon.repository.repository;


import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findAllByChatRoomOrderByCreatedAt(ChatRoom chatRoom);
}
