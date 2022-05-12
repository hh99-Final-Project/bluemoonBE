package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Alarm;
import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.ChatMessageDto;
import com.sparta.bluemoon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisRepository redisRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AlarmRepository alarmRepository;

    public void enter(Long userId, String roomId) {
        System.out.println("엔터@2222222222222222222222222222222222222222222222222222222222222222222222");
        // 채팅방 입장 정보 저장
        redisRepository.userEnterRoomInfo(userId, roomId);
        // 채팅방의 안 읽은 메세지의 수 초기화
        redisRepository.initChatRoomMessageInfo(roomId, userId);
    }

    //채팅
   public void sendMessage(ChatMessageDto chatMessageDto, User user) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomUuid(chatMessageDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다.")
        );
        ChatMessage chatMessage = new ChatMessage(user, chatMessageDto, chatRoom);
        chatMessageRepository.save(chatMessage);

        String topic = channelTopic.getTopic();
        String createdAt = getCurrentTime();
        chatMessageDto.setCreatedAt(createdAt);
        chatMessageDto.setType(ChatMessageDto.MessageType.TALK);
        chatMessageDto.setUserId(user.getId());

        redisTemplate.convertAndSend(topic, chatMessageDto);
    }
    //알람
    public void sendAlarm(ChatMessageDto chatMessageDto, User user) {
        Alarm alarm = new Alarm(chatMessageDto, user);
        alarmRepository.save(alarm);

        Long id = alarm.getId();
        String topic = channelTopic.getTopic();
        String createdAt = getCurrentTime();
        chatMessageDto.setMessageId(id);
        chatMessageDto.setCreatedAt(createdAt);
        chatMessageDto.setType(ChatMessageDto.MessageType.ENTER);

        redisTemplate.convertAndSend(topic, chatMessageDto);

    }

    //현재시간 추출 메소드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }

    public void updateUnReadMessageCount(ChatMessageDto chatMessageDto) {
        Long otherUserId = chatMessageDto.getOtherUserId();
        String roomId = chatMessageDto.getRoomId();
        // 상대방이 채팅방에 들어가 있지 않거나 들어가 있어도 나와 같은 대화방이 아닌 경우 안 읽은 메세지 처리를 할 것이다.
        if (!redisRepository.existChatRoomUserInfo(otherUserId) || !redisRepository.getUserEnterRoomId(otherUserId).equals(roomId)) {
            System.out.println("업데이트리드메세지카운트!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(otherUserId);
            System.out.println(redisRepository.getUserEnterRoomId(otherUserId));
            System.out.println(roomId);

            redisRepository.addChatRoomMessageCount(roomId, otherUserId);
            int unReadMessageCount = redisRepository
                .getChatRoomMessageCount(roomId, otherUserId);
            String topic = channelTopic.getTopic();

            ChatMessageDto chatMessageDto1 = new ChatMessageDto(chatMessageDto, unReadMessageCount);

            redisTemplate.convertAndSend(topic, chatMessageDto1);
        }
    }
}
