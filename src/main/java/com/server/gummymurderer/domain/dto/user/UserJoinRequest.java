package com.server.gummymurderer.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserJoinRequest {

    private String userId;
    private String userPassword;
    private String userNickname;

// user캐릭터 커스텀 관련 내용 필요

}
