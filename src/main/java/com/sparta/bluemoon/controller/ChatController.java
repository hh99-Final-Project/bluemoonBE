package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.ChatMessageDto;
import com.sparta.bluemoon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;

     //websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
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