package com.server.gummymurderer.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinUserResponse {

    private long userNo;
    private String userId;
    private String userNickname;
}