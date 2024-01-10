package com.server.gummymurderer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class AppException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = super.getMessage();
    }

    @Override
    public String toString() {
        return message;
    }
}
