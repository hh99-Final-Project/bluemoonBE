package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.domain.ChatRoomUser;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.ChatRoomResponseDto;
import com.sparta.bluemoon.dto.request.ChatRoomUserRequestDto;
import com.sparta.bluemoon.repository.repository.ChatMessageRepository;
import com.sparta.bluemoon.repository.repository.ChatRoomRepository;
import com.sparta.bluemoon.repository.repository.ChatRoomUserRepository;
import com.sparta.bluemoon.repository.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ChatRoomService {


    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    public void createChatRoom (
            ChatRoomUserRequestDto requestDto,
            UserDetailsImpl userDetails) {

        //상대방 방도 생성>상대방 찾기

        User anotherUser = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("상대방이 존재하지 않습니다.")
        );

        //roomHashCode 만들기
        int roomHashCode = createRoomHashCode(userDetails, anotherUser);

        //방 존재 확인 함수
        existRoom(roomHashCode, userDetails, anotherUser);

        //방 먼저 생성
        ChatRoom room = new ChatRoom(roomHashCode);
        chatRoomRepository.save(room);

        //내 방
        ChatRoomUser chatRoomUser = new ChatRoomUser(userDetails.getUser(), anotherUser, room);
        //다른 사람 방
        ChatRoomUser chatRoomAnotherUser = new ChatRoomUser(anotherUser, userDetails.getUser(), room);

        //저장
        chatRoomUserRepository.save(chatRoomUser);
        chatRoomUserRepository.save(chatRoomAnotherUser);
    }

    //for 둘 다 있는 방 판단
    public int createRoomHashCode(
            UserDetailsImpl userDetails,
            User anotherUser) {

        Long userId = userDetails.getUser().getId();
        Long anotherId = anotherUser.getId();
        return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
    }

    //이미 방이 존재할 때
    public void existRoom(
            int roomUsers,
            UserDetailsImpl userDetails,
            User anotherUser) {

        ChatRoom chatRoom = chatRoomRepository.findByRoomHashCode(roomUsers).orElse(null);

        if (chatRoom != null) {
            List<ChatRoomUser> chatRoomUser = chatRoom.getChatRoomUsers();
            if (chatRoomUser.size() == 2) {
                throw new IllegalArgumentException("이미 존재하는 방입니다.");
            } else if (chatRoomUser.size() == 1) {
                //나만 있을 때
                if (chatRoomUser.get(0).getUser().equals(userDetails.getUser())) {
                    ChatRoomUser user = new ChatRoomUser(anotherUser, userDetails.getUser(), chatRoom);
                    chatRoomUserRepository.save(user);
                } else {
                    //상대방만 있을 때
                    ChatRoomUser user = new ChatRoomUser(userDetails.getUser(), anotherUser, chatRoom);
                    chatRoomUserRepository.save(user);
                }
            }
        }
    }






}
