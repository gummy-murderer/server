package com.server.gummymurderer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),

    ;

    private HttpStatus status;
    private String message;
}
