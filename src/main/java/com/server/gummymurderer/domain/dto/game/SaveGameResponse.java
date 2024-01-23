package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameVoteEvent;
import lombok.Builder;

import java.util.List;

@Builder
public class SaveGameResponse {
    private GameSet gameSet;
    private GameNpc gameNpc;
    private GameVoteEvent gameVoteEvent;

    public SaveGameResponse(GameSet gameSet, GameNpc gameNpc, GameVoteEvent gameVoteEvent) {
        this.gameSet = gameSet;
        this.gameNpc = gameNpc;
        this.gameVoteEvent = gameVoteEvent;
    }
}
