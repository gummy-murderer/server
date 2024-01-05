package com.server.gummymurderer.domain.dto.user;

import com.server.gummymurderer.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReadUserResponse {

    private long userNo;
    private String userId;
    private String userNickname;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userLastModifiedAt;
    private LocalDateTime userDeletedAt;

    public static ReadUserResponse of(User user) {
        return ReadUserResponse.builder()
                .userNo(user.getUserNo())
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userCreatedAt(user.getCreatedAt())
                .userLastModifiedAt(user.getLastModifiedAt())
                .userDeletedAt(user.getDeletedAt())
                .build();
    }
}
