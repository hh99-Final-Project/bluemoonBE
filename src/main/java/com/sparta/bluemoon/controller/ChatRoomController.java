package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.ChatRoomResponseDto;
import com.sparta.bluemoon.dto.request.ChatRoomUserRequestDto;
import com.sparta.bluemoon.repository.RedisRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final RedisRepository redisRepository;

    //방생성
    @PostMapping ("/api/rooms")
    public void createChatRoom(
            @RequestBody ChatRoomUserRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomService.createChatRoom(requestDto, userDetails);

        Long chatPartnerUserId = requestDto.getUserId();
        Long myUserId = userDetails.getUser().getId();
        String roomId = requestDto.getRoomId();

        // redis repository에 채팅방에 존재하는 사람 마다 안 읽은 메세지의 갯수 초기화
        redisRepository.initChatRoomMessageInfo(roomId, myUserId);
        redisRepository.initChatRoomMessageInfo(roomId, chatPartnerUserId);
    }

    //내가 가진 채팅방 조회
    @GetMapping ("/api/rooms/{page}")
    public List<ChatRoomResponseDto> getChatRoom (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable int page) {
        page -= 1;
        return chatRoomService.getChatRoom(userDetails, page);
    }

}
