package com.sparta.bluemoon.service;

import com.sparta.bluemoon.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.bluemoon.domain.Alarm;
import com.sparta.bluemoon.domain.ChatMessage;
import com.sparta.bluemoon.domain.ChatRoom;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.ChatMessageDto;
import com.sparta.bluemoon.repository.AlarmRepository;
import com.sparta.bluemoon.repository.ChatMessageRepository;
import com.sparta.bluemoon.repository.ChatRoomRepository;
import com.sparta.bluemoon.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AlarmRepository alarmRepository;

    public void enter(Long userId, String roomId) {
        // 채팅방 입장 정보 저장
        redisRepository.userEnterRoomInfo(userId, roomId);
        // 채팅방의 안 읽은 메세지의 수 초기화
        redisRepository.initChatRoomMessageInfo(roomId, userId);
    }

    //채팅팅
   public void sendMessage(ChatMessageDto chatMessageDto) {
        User user = userRepository.findById(chatMessageDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomUuid(chatMessageDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다.")
        );
        ChatMessage chatMessage = new ChatMessage(user, chatMessageDto, chatRoom);
        chatMessageRepository.save(chatMessage);

        String topic = channelTopic.getTopic();
        String createdAt = getCurrentTime();
        chatMessageDto.setCreatedAt(createdAt);
        chatMessageDto.setType(ChatMessageDto.MessageType.TALK);

        redisTemplate.convertAndSend(topic, chatMessageDto);
    }
    //알람
    public void sendAlarm(ChatMessageDto chatMessageDto) {
       User user = userRepository.findById(chatMessageDto.getUserId()).orElseThrow(
               () -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
        Alarm alarm = new Alarm(chatMessageDto, user);
        alarmRepository.save(alarm);

        Long id = alarm.getId();
        String topic = channelTopic.getTopic();
        String createdAt = getCurrentTime();
        chatMessageDto.setId(id);
        chatMessageDto.setCreatedAt(createdAt);
        chatMessageDto.setType(ChatMessageDto.MessageType.ENTER);

        redisTemplate.convertAndSend(topic, chatMessageDto);

    }

    //현재시간 추출 메소드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }
}
