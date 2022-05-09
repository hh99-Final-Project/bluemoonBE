package com.sparta.bluemoon.service;

import com.sparta.bluemoon.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisRepository redisRepository;

    public void enter(Long userId, String roomId) {
        // 채팅방 입장 정보 저장
        redisRepository.userEnterRoomInfo(userId, roomId);
        // 채팅방의 안 읽은 메세지의 수 초기화
        redisRepository.initChatRoomMessageInfo(roomId, userId);
    }
}
