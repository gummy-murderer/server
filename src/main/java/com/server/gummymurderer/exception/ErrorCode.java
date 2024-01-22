package com.server.gummymurderer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러"),
    DUPLICATED_ID(HttpStatus.CONFLICT, "이미 등록되어있는 회원 ID 입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 등록되어있는 닉네임 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NPC_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 NPC를 찾을 수 없습니다."),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 game을 찾을 수 없습니다."),
    NO_CHAT_HISTORY(HttpStatus.NOT_FOUND, "채팅 내역이 없습니다."),
    AI_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서버와의 통신에 실패했습니다."),

    ;

    private HttpStatus status;
    private String message;
}
