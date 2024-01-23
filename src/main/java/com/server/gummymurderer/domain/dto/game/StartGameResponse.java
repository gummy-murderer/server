package com.server.gummymurderer.domain.dto.game;


import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
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

    private GameSet gameSet;
    private List<GameNpc> gameNpcList;

}
