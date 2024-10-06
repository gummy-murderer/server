package com.server.gummymurderer.domain.dto.member;

import com.server.gummymurderer.domain.dto.game.LoginGameSetDTO;
import com.server.gummymurderer.domain.entity.Authority;
import com.server.gummymurderer.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long memberNo;

    private String account;

    private String nickname;

    private List<Authority> roles = new ArrayList<>();

    private String token;

    private List<LoginGameSetDTO> loginGameSetDTO;

    public SignResponse(Member member, List<LoginGameSetDTO> loginGameSetDTO) {
        this.memberNo = member.getMemberNo();
        this.account = member.getAccount();
        this.nickname = member.getNickname();
        this.roles = member.getRoles();
        this.loginGameSetDTO = loginGameSetDTO;
    }

    public static SignResponse of(Member member, List<LoginGameSetDTO> loginGameSetDTOList, String token) {
        return SignResponse.builder()
                .memberNo(member.getMemberNo())
                .account(member.getAccount())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .token(token)
                .loginGameSetDTO(loginGameSetDTOList)
                .build();
    }

    public static SignResponse of(Member member) {
        return SignResponse.builder()
                .memberNo(member.getMemberNo())
                .account(member.getAccount())
                .nickname(member.getNickname())
                .roles(member.getRoles())
                .build();
    }
}