package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.dto.ChatRoomResponseDto;
import com.sparta.bluemoon.dto.request.ChatRoomUserRequestDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.repository.ChatRoomRepository;
import com.sparta.bluemoon.repository.RedisRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.bluemoon.exception.ErrorCode.CANNOT_FOUND_CHATROOM;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final RedisRepository redisRepository;
    private final ChatRoomRepository chatRoomRepository;

    //방생성
    @PostMapping ("/api/rooms")
    public String createChatRoom(
        @RequestBody ChatRoomUserRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String chatRoomUuid = chatRoomService.createChatRoom(requestDto, userDetails);
        Long chatPartnerUserId = requestDto.getUserId();
        Long myUserId = userDetails.getUser().getId();


        // redis repository에 채팅방에 존재하는 사람 마다 안 읽은 메세지의 갯수 초기화
        redisRepository.initChatRoomMessageInfo(chatRoomUuid, myUserId);
        redisRepository.initChatRoomMessageInfo(chatRoomUuid, chatPartnerUserId);
        return chatRoomUuid;

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
                () -> new CustomException(CANNOT_FOUND_CHATROOM)
        );

        chatRoomService.deleteChatRoom(chatroom, userDetails.getUser());
    }

    //이전 채팅 메시지 불러오기
    @GetMapping("/api/rooms/{roomId}/messages")
    public List<ChatMessage> getPreviousChatMessage(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return chatRoomService.getPreviousChatMessage(roomId, userDetails);
    }

    //채팅방 입장시 상대정보 조회
    @GetMapping("/api/rooms/otherUserInfo")
    public ChatRoomOtherUserInfoResponseDto getOtherUserInfo(
            @RequestBody ChatRoomOtherUserInfoRequestDto chatRoomOtherUserInfoRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return chatRoomService.getOtherUserInfo(chatRoomOtherUserInfoRequestDto, userDetails);
    }
}
