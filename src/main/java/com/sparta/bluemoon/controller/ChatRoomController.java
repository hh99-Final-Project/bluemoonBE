package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.dto.ChatRoomResponseDto;
import com.sparta.bluemoon.dto.request.ChatRoomUserRequestDto;
import com.sparta.bluemoon.repository.ChatMessageRepository;
import com.sparta.bluemoon.repository.ChatRoomRepository;
import com.sparta.bluemoon.repository.ChatRoomUserRepository;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    //방생성
    @PostMapping ("/api/rooms")
    public void createChatRoom(
            @RequestBody ChatRoomUserRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomService.createChatRoom(requestDto, userDetails);
    }

    //내가 가진 채팅방 조회
    @GetMapping ("/api/rooms/{page}")
    public List<ChatRoomResponseDto> getChatRoom (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                  @PathVariable int page) {
        page -= 1;
        return chatRoomService.getChatRoom(userDetails, page);
    }

    //채팅방 삭제
    @DeleteMapping("api/rooms/{roomId}")
    public void deleteChatRoom(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //roonId=uuid
        //방번호랑 나간 사람
        ChatRoom chatroom = chatRoomRepository.findByChatRoomUuid(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        chatRoomService.deleteChatRoom(chatroom, userDetails.getUser());
    }
    

}
