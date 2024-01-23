package com.server.gummymurderer.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class JoinMemberRequest {

    private String account;
    private String password;
    private String nickname;

// member캐릭터 커스텀 관련 내용 필요

}
