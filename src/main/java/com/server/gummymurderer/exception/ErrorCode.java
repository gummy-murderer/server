package com.server.gummymurderer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB 에러"),
    DUPLICATED_ACCOUNT(HttpStatus.CONFLICT, "이미 등록되어있는 회원 계정 입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 등록되어있는 닉네임 입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 등록되어 있는 이메일 입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    NPC_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 NPC를 찾을 수 없습니다."),
    INVALID_ACCOUNT_OR_PASSWORD( HttpStatus.CONFLICT, "계정 혹은 비밀번호가 틀렸습니다."),
    SAVED_GAME_FULL(HttpStatus.CONFLICT, "게임 저장 가능 슬롯을 초과했습니다."),
    GAME_SET_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 게임 세팅을 찾을 수 없습니다."),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 game을 찾을 수 없습니다."),
    NO_CHAT_HISTORY(HttpStatus.NOT_FOUND, "채팅 내역이 없습니다."),
    AI_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AI 서버와의 통신에 실패했습니다."),
    INVALID_ACCOUNT(HttpStatus.CONFLICT, "잘못된 계정입니다."),
    INVALID_MEMBER_REGISTRATION_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 회원가입 요청입니다."),

    ;

    private HttpStatus status;
    private String message;
}
