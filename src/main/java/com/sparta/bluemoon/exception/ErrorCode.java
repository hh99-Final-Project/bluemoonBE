package com.sparta.bluemoon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //USER
    NOT_FOUND_USER(HttpStatus.NOT_FOUND.value(), "U001", "해당 유저를 찾을 수 없습니다."),

    //CHAT-USER
    NOT_FOUND_USER_IN_CHAT(HttpStatus.NOT_FOUND.value(), "CU001","해당 유저를 찾을 수 없습니다."),

    //POST
    CANNOT_DELETE_NOT_EXIST_POST(HttpStatus.NOT_FOUND.value(), "P001","해당 게시물을 찾을 수 없습니다."),
    CANNOT_FIND_POST_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "P002","해당 게시물을 찾을 수 없습니다."),
    ONLY_CAN_DELETE_POST_WRITER(HttpStatus.FORBIDDEN.value(), "P003","게시글 작성자 만이 게시글을 삭제할 수 있습니다."),
    DOESNT_EXIST_MAIN_POST_FOR_ANONYMOUS(HttpStatus.NOT_FOUND.value(), "P004","남이 쓴 게시글이 존재하지 않습니다."),
    DOESNT_EXIST_POST_FOR_ANONYMOUS(HttpStatus.NOT_FOUND.value(), "P005","해당하는 게시글이 존재하지 않습니다."),

    //CHATROOM
    NOT_FOUND_ANOTHER_USER(HttpStatus.NOT_FOUND.value(), "R001","해당하는 게시글이 존재하지 않습니다."),
    ROOM_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "R002","이미 존재하는 방입니다."),
    CANNOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND.value(), "R003","존재하지 않는 채팅방입니다."),
    FORBIDDEN_CHATROOM(HttpStatus.FORBIDDEN.value(), "R004","접근 불가능한 채팅방 입니다."),
    CANNOT_MAKE_ROOM_ALONE(HttpStatus.BAD_REQUEST.value(), "R005","자기자신에게 채팅을 신청할 수 없습니다"),

    //COMMENT
    DOESNT_EXIST_POST_FOR_WRITE(HttpStatus.NOT_FOUND.value(), "C001","해당하는 게시글이 존재하지 않습니다."),
    DOESNT_EXIST_POST_FOR_DELETE(HttpStatus.NOT_FOUND.value(), "C002","해당하는 게시글이 존재하지 않습니다."),
    ONLY_CAN_DELETE_COMMENT_WRITER(HttpStatus.FORBIDDEN.value(), "C003","글을 작성한 유저만 삭제할 수 있습니다."),

    //VOICE
    VOICE_FILE_INVALID(HttpStatus.BAD_REQUEST.value(), "V001","잘못된 파일 형식입니다."),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

    }

