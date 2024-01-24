package com.server.gummymurderer.domain.dto.game;


import com.server.gummymurderer.domain.dto.gameNpc.GameNpcDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartGameResponse {

    private String playerName;
    private String playerNickName;
    private Long gameStatus;
    private Long gameSetNo;
    private List<GameNpcDTO> gameNpcList;

}
