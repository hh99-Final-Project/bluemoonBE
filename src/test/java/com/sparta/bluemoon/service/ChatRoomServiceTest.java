package com.sparta.bluemoon.service;

import com.sparta.bluemoon.chat.ChatMessageRepository;
import com.sparta.bluemoon.chat.RedisRepository;
import com.sparta.bluemoon.chatRoom.*;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.chatRoom.responseDto.ChatRoomResponseDto;
import com.sparta.bluemoon.chatRoom.requestDto.ChatRoomUserRequestDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatRoomServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatRoomUserRepository chatRoomUserRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    @Test
    @Order(1)
    @DisplayName("채팅방 생성")
    void createChatRoom() {
        //given
        userRepository.save(new User("1234@naver.com", "가나다라", "1234"));
        userRepository.save(new User("1234@gmail.com", "아자차카", "1234"));
        User user = userRepository.findByNickname("가나다라").get();
        User otherUser = userRepository.findByNickname("아자차카").get();

        //when
        ChatRoomUserRequestDto chatRoomUserRequestDto = new ChatRoomUserRequestDto();
        chatRoomUserRequestDto.setUserId(otherUser.getId());
        chatRoomService.createChatRoom(chatRoomUserRequestDto, new UserDetailsImpl(user));

        //then
        assertNotNull(chatRoomRepository.findAll());
    }

    @Test
    @Order(2)
    @DisplayName("roomHashCode 만들기")
    void createRoomHashCode() {
        //given
        User user = userRepository.findByNickname("가나다라").get();
        User otherUser = userRepository.findByNickname("아자차카").get();

        //when
        //실제 로직에서 해쉬코드를 만드는 메소드
        int roomHashCode = chatRoomService.createRoomHashCode(new UserDetailsImpl(user), otherUser);

        //유저와 다른 유저로 만든 해쉬코드
        Long userId = user.getId();
        Long otherUserId = otherUser.getId();
        int hashCode = userId > otherUserId ? Objects.hash(userId, otherUserId) : Objects.hash(otherUserId, userId);

        //then
        assertEquals(roomHashCode, hashCode);
    }

    @Test
    @Order(3)
    @DisplayName("방 존재여부 확인")
    @Transactional(readOnly = true)
    void existRoom() {
        //given
        User user = userRepository.findByNickname("가나다라").get();
        User otherUser = userRepository.findByNickname("아자차카").get();
        Long userId = user.getId();
        Long otherUserId = otherUser.getId();
        int hashCode = userId > otherUserId ? Objects.hash(userId, otherUserId) : Objects.hash(otherUserId, userId);

        //when
        boolean check = chatRoomService.existRoom(hashCode, new UserDetailsImpl(user), otherUser);

        //then
        //이미 위에서 채팅방을 생성했기 때문에 true값이 출려된다.
        assertTrue(check);
    }

    @Test
    void getChatRoom() {
        //given
        User user = userRepository.findByNickname("가나다라").get();
        int page = 0;
        int display = 5;
        Pageable pageable = PageRequest.of(page,display);

        //when
        ChatRoomResponseDto chatRoomResponseDto = chatRoomService.getChatRoom(new UserDetailsImpl(user), page).get(0);
        Page<ChatRoomUser> chatRoomUser = chatRoomUserRepository.findAllByUser(user, pageable);
        ChatRoom chatRoom = chatRoomRepository.findAll().get(0);

        //then
        assertEquals(chatRoomResponseDto.getChatRoomUuid(), chatRoom.getChatRoomUuid());

    }

    @Test
    void createChatRoomDto() {
    }

    @Test
    void deleteChatRoom() {
    }

    @Test
    void getOtherUserInfo() {
    }

    @Test
    void getPreviousChatMessage() {
    }
}