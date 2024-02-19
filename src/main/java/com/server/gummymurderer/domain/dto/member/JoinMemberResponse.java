package com.server.gummymurderer.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinMemberResponse {

    private Long memberNo;
    private String account;
    private String name;
}