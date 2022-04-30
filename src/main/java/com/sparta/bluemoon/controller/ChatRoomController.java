package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.ChatRoomResponseDto;
import com.sparta.bluemoon.dto.request.ChatRoomUserRequestDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //방생성
    @PostMapping ("/rooms")
    public void createChatRoom(
            @RequestBody ChatRoomUserRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomService.createChatRoom(requestDto, userDetails);
    }

    //내가 가진 채팅방 조회
    @GetMapping ("/rooms")
    public List<ChatRoomResponseDto> getChatRoom (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getChatRoom(userDetails);
    }

}
