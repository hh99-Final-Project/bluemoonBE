package com.sparta.bluemoon.chat;

import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.security.jwt.JwtDecoder;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import static com.sparta.bluemoon.exception.ErrorCode.NOT_FOUND_USER_IN_CHAT;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    /**
     * websocket "/pub/chat/enter"로 들어오는 메시징을 처리한다.
     * 채팅방에 입장했을 경우
     */
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto chatMessageDto, @Header("token") String token) {
        String username = jwtDecoder.decodeUsername(token.substring(7));
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER_IN_CHAT)
        );

        chatService.enter(user.getId(), chatMessageDto.getRoomId());
    }

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto chatMessageDto, @Header("token") String token) {
        String username = jwtDecoder.decodeUsername(token.substring(7));
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        chatService.sendMessage(chatMessageDto, user);
        chatService.updateUnReadMessageCount(chatMessageDto);
    }
    //알람
    @MessageMapping("/chat/alarm")
    public void alarm(ChatMessageDto chatMessageDto, @Header("token") String token){
        String username = jwtDecoder.decodeUsername(token.substring(7));
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        chatService.sendAlarm(chatMessageDto, user);
    }
}