package com.server.gummymurderer.domain.dto.member;

import com.server.gummymurderer.domain.dto.game.LoginGameSetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SteamLoginResponse {

    private String token;
    private List<LoginGameSetDTO> loginGameSetDTO;

}