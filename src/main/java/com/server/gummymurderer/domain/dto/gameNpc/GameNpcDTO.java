package com.server.gummymurderer.domain.dto.gameNpc;

import com.server.gummymurderer.domain.entity.GameNpc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameNpcDTO {

    private long gameNpcNo;
    private String npcName;
    private String npcJob;
    private String npcPersonality;
    private String npcFeature;
    private String npcStatus;
    private Float npcDeathLocationX;
    private Float npcDeathLocationY;
    private Float npcDeathLocationZ;
    private long npcDeathNightNumber;


    public  GameNpcDTO(GameNpc gameNpc) {
        this.gameNpcNo = gameNpc.getGameNpcNo();
        this.npcName = gameNpc.getNpcName();
        this.npcJob = gameNpc.getNpcJob();
        this.npcPersonality = gameNpc.getNpcPersonality();
        this.npcFeature = gameNpc.getNpcFeature();
        this.npcStatus = gameNpc.getNpcStatus();
        this.npcDeathLocationX = gameNpc.getNpcDeathLocationX();
        this.npcDeathLocationY = gameNpc.getNpcDeathLocationY();
        this.npcDeathLocationZ = gameNpc.getNpcDeathLocationZ();
        this.npcDeathNightNumber = gameNpc.getNpcDeathNightNumber();
        this.npcToken = gameNpc.getNpcToken();
    }



}
