package com.sparta.bluemoon.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(HttpStatus.BAD_REQUEST.value(), "U001", "해당 유저를 찾을 수 없습니다."),

    NOT_FOUND_USER_IN_CHAT(HttpStatus.BAD_REQUEST.value(), "CU001","해당 유저를 찾을 수 없습니다."),

    CANNOT_DELETE_NOT_EXSIST_POST(HttpStatus.BAD_REQUEST.value(), "P001","해당 게시물을 찾을 수 없습니다."),

    CANNOT_FIND_POST_NOT_EXSIST(HttpStatus.BAD_REQUEST.value(), "P002","해당 게시물을 찾을 수 없습니다."),

    ONLY_CAN_DELETE_POST_WRITER(HttpStatus.BAD_REQUEST.value(), "P003","게시글 작성자 만이 게시글을 삭제할 수 있습니다."),

    DOESNT_EXSIST_OTHER_USER_POST(HttpStatus.BAD_REQUEST.value(), "P004","남이 쓴 게시글이 존재하지 않습니다."),

    DOESNT_EXSIST_POST_FOR_ANONYMOUS(HttpStatus.BAD_REQUEST.value(), "P005","해당하는 게시글이 존재하지 않습니다."),

    NOT_FOUND_ANOTHER_USER(HttpStatus.BAD_REQUEST.value(), "R001","해당하는 게시글이 존재하지 않습니다."),

    ROOM_ALREADY_EXSIST(HttpStatus.BAD_REQUEST.value(), "R002","이미 존재하는 방입니다."),

    DOESNT_EXSIST_POST_FOR_WRITE(HttpStatus.BAD_REQUEST.value(), "C001","해당하는 게시글이 존재하지 않습니다."),

    DOESNT_EXSIST_POST_FOR_DELETE(HttpStatus.BAD_REQUEST.value(), "C002","해당하는 게시글이 존재하지 않습니다."),

    ONLY_CAN_DELETE_COMMENT_WRITER(HttpStatus.BAD_REQUEST.value(), "C003","글을 작성한 유저만 삭제할 수 있습니다."),

    VOICE_FILE_INVALID(HttpStatus.BAD_REQUEST.value(), "V001","잘못된 파일 형식입니다."),

    CANNOT_FOUND_CHATROOM(HttpStatus.BAD_REQUEST.value(), "CR001","존재하지 않는 채팅방입니다."),

    FORBIDDEN_CHATROOM(HttpStatus.BAD_REQUEST.value(), "CR002","접근 불가능한 채팅방 입니다."),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

    }

