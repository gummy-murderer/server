package com.server.gummymurderer.domain.dto.member;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SteamLoginRequest {

    private String steamId;
    private String nickname;
    private String authTicket;

}