package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.ChatMessageDto;
import com.sparta.bluemoon.dto.request.ChatMessageEnterDto;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.security.jwt.JwtDecoder;
import com.sparta.bluemoon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatService chatService;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    /**
     * websocket "/pub/chat/enter"로 들어오는 메시징을 처리한다.
     * 채팅방에 입장했을 경우
     */
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageEnterDto chatMessageEnterDto, @Header("token") String token) {
        String username = jwtDecoder.decodeUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        chatService.enter(user.getId(), chatMessageEnterDto.getRoomId());
        String topic = channelTopic.getTopic();
        redisTemplate.convertAndSend(topic, chatMessageEnterDto);
    }

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto chatMessageDto, @Header("token") String token) {
        String username = jwtDecoder.decodeUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        chatService.sendMessage(chatMessageDto);
        chatService.updateUnReadMessageCount(chatMessageDto);
    }
    //알람
    @MessageMapping("/chat/alarm")
    public void alarm(ChatMessageDto chatMessageDto){
        chatService.sendAlarm(chatMessageDto);
    }
}