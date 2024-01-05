package com.server.gummymurderer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    DUPLICATED_ID(HttpStatus.CONFLICT, "이미 등록되어있는 회원 ID 입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 등록되어있는 닉네임 입니다.");

    private HttpStatus status;
    private String message;
}
