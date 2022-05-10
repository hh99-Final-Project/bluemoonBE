package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.ChatMessageDto;
import com.sparta.bluemoon.dto.request.ChatMessageEnterDto;
import com.sparta.bluemoon.exception.CustomException;
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

import static com.sparta.bluemoon.exception.ErrorCode.NOT_FOUND_USER;
import static com.sparta.bluemoon.exception.ErrorCode.NOT_FOUND_USER_IN_CHAT;

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
                () -> new CustomException(NOT_FOUND_USER_IN_CHAT)
        );

        chatService.enter(user.getId(), chatMessageEnterDto.getRoomId());
        String topic = channelTopic.getTopic();
        redisTemplate.convertAndSend(topic, chatMessageEnterDto);
    }

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto chatMessageDto) {
        chatService.sendMessage(chatMessageDto);
    }
    //알람
    @MessageMapping("/chat/alarm")
    public void alarm(ChatMessageDto chatMessageDto){
        chatService.sendAlarm(chatMessageDto);
    }
}