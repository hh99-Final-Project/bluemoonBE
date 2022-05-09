package com.sparta.bluemoon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RedisRepository {

    public static final String USER_INFO = "USER_INFO"; // session id와 user id를 mapping
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    // 유저가 입장한 채팅방 정보 저장
    // "ENTER_INFO", "session id", "user id"
    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, Long> hashOpsEnterInfo;

    // 채팅방 마다 유저가 안 읽은 메세지 갯수에 대한 정보 저장
    @Resource(name = "redisTemplate")
    // roomId, userId, 안 읽은 메세지 갯수
    private HashOperations<Long, Long, Integer> hashOpsChatRoomMessageInfo;

    // 나의 대화상대 정보를 session id로 확인
    @Resource(name = "redisTemplate")
    // USER_INFO, sessionId, userId
    private HashOperations<String, String, Long> hashOpsUserInfo;

    // step1
    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(Long userUuid, Long roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, userUuid, roomId);
    }

    // 사용자가 채팅방에 입장해 있는지 확인
    public boolean existChatRoomUserInfo(Long userUuid) {
        return hashOpsEnterInfo.hasKey(ENTER_INFO, userUuid);
    }

    // 사용자가 입장해 있는 채팅방 ID 조회
    public Long getUserEnterRoomId(Long userUuid) {
        return hashOpsEnterInfo.get(ENTER_INFO, userUuid);
    }

    // 사용자가 입장해 있는 채팅방 ID 조회
    public void exitUserEnterRoomId(Long userUuid) {
        hashOpsEnterInfo.delete(ENTER_INFO, userUuid);
    }

    // step2
    // 채팅방에서 사용자가 읽지 않은 메세지의 갯수 초기화
    public void initChatRoomMessageInfo(Long roomId, Long userId) {
        hashOpsChatRoomMessageInfo.put(roomId, userId, 0);
    }

    // 채팅방에서 사용자가 읽지 않은 메세지의 갯수 추가
    public void addChatRoomMessageCount(String roomId, String userUuid) {
        hashOpsChatRoomMessageInfo.put(roomId, userUuid, hashOpsChatRoomMessageInfo.get(roomId, userUuid) + 1);
    }

    //
    public int getChatRoomMessageCount(Long roomId, String userUuid) {
        return hashOpsChatRoomMessageInfo.get(roomId, userUuid);
    }

    // step3
    // 나의 대화상대 정보 저장
    public void saveMyInfo(String sessionId, Long userId) {
        hashOpsUserInfo.put(USER_INFO, sessionId, userId);
    }

    public Long getMyInfo(String sessionId) {
        return hashOpsUserInfo.get(USER_INFO, sessionId);
    }

    // 나의 대화상대 정보 삭제
    public void deleteMyInfo(String sessionId) {
        hashOpsUserInfo.delete(USER_INFO, sessionId);
    }
}