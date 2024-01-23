package com.server.gummymurderer.domain.dto.game;

import lombok.Getter;

@Getter
public class SaveGameRequest {

    private Long gameSetNo;
    private String voteNpcName;
    private String voteResult;
    private Long voteNightNumber;

    private String victim;
    private float npcDeathLocationX;
    private float npcDeathLocationY;
    private float npcDeathLocationZ;

}
