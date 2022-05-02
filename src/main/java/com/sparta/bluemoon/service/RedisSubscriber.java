package com.sparta.bluemoon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bluemoon.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            System.out.println("om message에서 잡아서 진행합니다");
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // ChatMessageDto 객채로 맵핑
            ChatMessageDto roomMessage = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            // Websocket 구독자에게 채팅 메시지 Send

            System.out.println("roomMessage = " + roomMessage.getType());
            if (roomMessage.getType().equals(ChatMessageDto.MessageType.ENTER)) {
                System.out.println("Enter에 걸린게 맞나요?");
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getUserId(), roomMessage);
            } else {
                System.out.println("아니요 ENTER에 걸리지 않았습니다.");
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}