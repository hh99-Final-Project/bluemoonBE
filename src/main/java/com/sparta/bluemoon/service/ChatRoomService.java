package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.domain.ChatRoomUser;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.ChatRoomResponseDto;
import com.sparta.bluemoon.dto.request.ChatRoomUserRequestDto;
import com.sparta.bluemoon.dto.response.ChatRoomOtherUserInfoResponseDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.repository.ChatMessageRepository;
import com.sparta.bluemoon.repository.ChatRoomRepository;
import com.sparta.bluemoon.repository.ChatRoomUserRepository;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.util.Calculator;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.sparta.bluemoon.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ChatRoomService {


    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    //채팅방 생성
    public String createChatRoom (
            ChatRoomUserRequestDto requestDto,
            UserDetailsImpl userDetails) {
        //상대방 방도 생성>상대방 찾기
        if(requestDto.getUserId().equals(userDetails.getUser().getId())) throw  new CustomException(CANNOT_MAKE_ROOM_ALONE);
        User anotherUser = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new CustomException(NOT_FOUND_ANOTHER_USER)
        );

        //roomHashCode 만들기
        int roomHashCode = createRoomHashCode(userDetails, anotherUser);
        System.out.println(roomHashCode);

        //방 존재 확인 함수
        existRoom(roomHashCode, userDetails, anotherUser);

        //방 먼저 생성
        ChatRoom room = new ChatRoom(roomHashCode);
        chatRoomRepository.save(room);
        System.out.println("서비스"+room.getId());
        System.out.println("서비스"+room);

        //내 방
        ChatRoomUser chatRoomUser = new ChatRoomUser(userDetails.getUser(), anotherUser, room);
        //다른 사람 방
        ChatRoomUser chatRoomAnotherUser = new ChatRoomUser(anotherUser, userDetails.getUser(), room);

        //저장
        chatRoomUserRepository.save(chatRoomUser);
        chatRoomUserRepository.save(chatRoomAnotherUser);

        return room.getChatRoomUuid();
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
                throw new CustomException(ROOM_ALREADY_EXIST);
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



    //채팅방 조회
    public List<ChatRoomResponseDto> getChatRoom(UserDetailsImpl userDetails, int page) {
        //user로 챗룸 유저를 찾고>>챗룸 유저에서 채팅방을 찾는다
        //마자
        //마지막나온 메시지 ,내용 ,시간
        int display = 5;
        Pageable pageable = PageRequest.of(page,display);
        List<ChatRoomResponseDto> responseDtos = new ArrayList<>();
        Page<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByUser(userDetails.getUser(),pageable);

        //TODO:챗 유저로 받아야하나??chatRoomUsers.getContent();

        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            ChatRoom chatRoom = chatRoomUser.getChatRoom();
            ChatRoomResponseDto responseDto = createChatRoomDto(chatRoomUser);
            responseDtos.add(responseDto);

            //정렬
            responseDtos.sort(Collections.reverseOrder());
        }
        return responseDtos;
    }


    public ChatRoomResponseDto createChatRoomDto(ChatRoomUser chatRoomUser) {
        String roomName = chatRoomUser.getName();
        String roomId = chatRoomUser.getChatRoom().getChatRoomUuid();
        System.out.println(roomId);
        String lastMessage;
        LocalDateTime lastTime;
        //마지막
        List<ChatMessage> Messages = chatMessageRepository.findAllByChatRoomOrderByCreatedAtDesc(chatRoomUser.getChatRoom());
        //메시지 없을 때 디폴트
        if (Messages.isEmpty()) {
            lastMessage = "채팅방이 생성 되었습니다.";
            lastTime = LocalDateTime.now();
        } else {
            lastMessage = Messages.get(0).getMessage();
            lastTime = Messages.get(0).getCreatedAt();
        }
        long dayBeforeTime = ChronoUnit.MINUTES.between(lastTime, LocalDateTime.now());
        String dayBefore = Calculator.time(dayBeforeTime);
        return new ChatRoomResponseDto(roomName, roomId, lastMessage, lastTime, dayBefore);
    }


    //채팅방 삭제
    @Transactional
    public void deleteChatRoom(ChatRoom chatroom, User user) {
        if (chatroom.getChatRoomUsers().size()!=1) {
            chatRoomUserRepository.deleteByChatRoomAndUser(chatroom, user);
        } else if (chatroom.getChatRoomUsers().size()==1){
            chatRoomRepository.delete(chatroom);
        }
//        else{
//            throw new IllegalArgumentException("존재하지 않는 채팅방 입니다.");
//        }
    }
    //채팅방 입장시 상대 유저 정보 조회
    public ChatRoomOtherUserInfoResponseDto getOtherUserInfo(String roomId, UserDetailsImpl userDetails){
        User myUser = userDetails.getUser();
        List<ChatRoomUser> users = chatRoomRepository.findByChatRoomUuid(roomId).get().getChatRoomUsers();
        for(ChatRoomUser user : users){
            if(!user.getUser().getId().equals(myUser.getId())) {
                System.out.println(user.getUser().getId());
                System.out.println(myUser.getId());
               User otherUser = user.getUser();
               return new ChatRoomOtherUserInfoResponseDto(otherUser);
            }
        }
        throw new CustomException(DOESNT_EXIST_OTHER_USER);
    }

    //채팅방 이전 대화내용 불러오기
    public List<ChatMessage> getPreviousChatMessage(String roomId, UserDetailsImpl userDetails) {
        ChatRoom chatroom = chatRoomRepository.findByChatRoomUuid(roomId).orElseThrow(
                () -> new CustomException(CANNOT_FOUND_CHATROOM)
        );
        List<ChatRoomUser> chatRoomUsers = chatroom.getChatRoomUsers();
        //혹시 채팅방 이용자가 아닌데 들어온다면,
        for(ChatRoomUser chatroomUser:chatRoomUsers){
            if(chatroomUser.getUser().getId().equals(userDetails.getUser().getId())) {
                System.out.println(chatroomUser.getUser().getId());
                System.out.println(userDetails.getUser().getId());
                return chatMessageRepository.findAllByChatRoomOrderByCreatedAtAsc(chatroom);
            }
        }
        throw new CustomException(FORBIDDEN_CHATROOM);
    }
}
