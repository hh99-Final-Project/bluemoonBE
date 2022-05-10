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
    // "ENTER_INFO", "user id", "room id"
    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, String> hashOpsEnterInfo;

    // 채팅방 마다 유저가 안 읽은 메세지 갯수에 대한 정보 저장
    @Resource(name = "redisTemplate")
    // roomId, userId, 안 읽은 메세지 갯수
    private HashOperations<String, Long, Integer> hashOpsChatRoomMessageInfo;

    // 나의 대화상대 정보를 session id로 확인
    @Resource(name = "redisTemplate")
    // USER_INFO, sessionId, userId
    private HashOperations<String, String, Long> hashOpsUserInfo;

    // step1
    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void userEnterRoomInfo(Long userId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, userId, roomId);
    }

    // 사용자가 채팅방에 입장해 있는지 확인
    public boolean existChatRoomUserInfo(Long userId) {
        return hashOpsEnterInfo.hasKey(ENTER_INFO, userId);
    }

    // 사용자가 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(Long userId) {
        return hashOpsEnterInfo.get(ENTER_INFO, userId);
    }

    // 사용자가 입장해 있는 채팅방 ID 조회
    public void exitUserEnterRoomId(Long userId) {
        hashOpsEnterInfo.delete(ENTER_INFO, userId);
    }

    // step2
    // 채팅방에서 사용자가 읽지 않은 메세지의 갯수 초기화
    public void initChatRoomMessageInfo(String roomId, Long userId) {
        hashOpsChatRoomMessageInfo.put(roomId, userId, 0);
    }

    // 채팅방에서 사용자가 읽지 않은 메세지의 갯수 추가
    public void addChatRoomMessageCount(String roomId, Long userId) {
        hashOpsChatRoomMessageInfo.put(roomId, userId, hashOpsChatRoomMessageInfo.get(roomId, userId) + 1);
    }

    //
    public int getChatRoomMessageCount(String roomId, Long userId) {
        return hashOpsChatRoomMessageInfo.get(roomId, userId);
    }

    // step3
    // 나의 대화상대 정보 저장
    public void saveMyInfo(String sessionId, Long userId) {
        hashOpsUserInfo.put(USER_INFO, sessionId, userId);
    }

    public boolean existMyInfo(String sessionId) {
        return hashOpsUserInfo.hasKey(USER_INFO, sessionId);
    }

    public Long getMyInfo(String sessionId) {
        return hashOpsUserInfo.get(USER_INFO, sessionId);
    }

    // 나의 대화상대 정보 삭제
    public void deleteMyInfo(String sessionId) {
        hashOpsUserInfo.delete(USER_INFO, sessionId);
    }
}