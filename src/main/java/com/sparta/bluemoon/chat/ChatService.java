package com.sparta.bluemoon.chat;

import com.sparta.bluemoon.chat.requestDto.ChatMessageDto.MessageType;
import com.sparta.bluemoon.chatRoom.ChatRoomRepository;
import com.sparta.bluemoon.chatRoom.ChatRoom;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.chat.requestDto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.transaction.annotation.Transactional;

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
        // 채팅방 입장 정보 저장
        redisRepository.userEnterRoomInfo(userId, roomId);
        // 채팅방의 안 읽은 메세지의 수 초기화
        redisRepository.initChatRoomMessageInfo(roomId, userId);
        System.out.println("redisRepository.getUserEnterRoomId(userId) = " + redisRepository
            .getUserEnterRoomId(userId));
        System.out.println("redisRepository = " + redisRepository.getChatRoomMessageCount(roomId, userId));
    }

    //채팅
    @Transactional
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
        // front에서 요청해서 진행한 작업 나의 userId 넣어주기
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
        chatMessageDto.setType(MessageType.COMMENT_ALARM);

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

    //안읽은 메세지 업데이트
    public void updateUnReadMessageCount(ChatMessageDto requestChatMessageDto) {
        Long otherUserId = requestChatMessageDto.getOtherUserId();
        String roomId = requestChatMessageDto.getRoomId();
        // 상대방이 채팅방에 들어가 있지 않거나 들어가 있어도 나와 같은 대화방이 아닌 경우 안 읽은 메세지 처리를 할 것이다.
        if (!redisRepository.existChatRoomUserInfo(otherUserId) || !redisRepository.getUserEnterRoomId(otherUserId).equals(roomId)) {

            redisRepository.addChatRoomMessageCount(roomId, otherUserId);
            int unReadMessageCount = redisRepository
                .getChatRoomMessageCount(roomId, otherUserId);
            String topic = channelTopic.getTopic();

            ChatMessageDto responseChatMessageDto = new ChatMessageDto(requestChatMessageDto, unReadMessageCount);

            redisTemplate.convertAndSend(topic, responseChatMessageDto);
        }
    }
}
