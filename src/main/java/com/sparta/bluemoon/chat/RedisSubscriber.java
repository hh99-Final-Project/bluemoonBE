package com.sparta.bluemoon.chat;

import static com.sparta.bluemoon.exception.ErrorCode.INVALID_MESSAGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
import com.sparta.bluemoon.chat.requestDto.ChatMessageDto.MessageType;
import com.sparta.bluemoon.chat.responseDto.AlarmResponseDto;
import com.sparta.bluemoon.chat.responseDto.MessageResponseDto;
import com.sparta.bluemoon.chat.responseDto.UnreadMessageCount;
import com.sparta.bluemoon.exception.CustomException;
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
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            // ChatMessageDto 객채로 맵핑
            ChatMessageDto roomMessage = objectMapper.readValue(publishMessage, ChatMessageDto.class);

            // Websocket 구독자에게 채팅 메시지 Send

            // 댓글 작성시 알람 메세지 (종 모양 알람 메세지)
            if (roomMessage.getType().equals(MessageType.COMMENT_ALARM)) {
                AlarmResponseDto alarmResponseDto = new AlarmResponseDto(roomMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getOtherUserId(), alarmResponseDto);
            // 안읽은 메세지 알람 표시 (별 모양)
            } else if (roomMessage.getType().equals(MessageType.UNREAD_MESSAGE_COUNT_ALARM)) {
                UnreadMessageCount unreadMessageCount = new UnreadMessageCount(roomMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getOtherUserId(), unreadMessageCount);
            //채팅 메세지
            } else {
                MessageResponseDto messageResponseDto = new MessageResponseDto(roomMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), messageResponseDto);
            }
        } catch (Exception e) {
            throw new CustomException(INVALID_MESSAGE);
        }
    }
}