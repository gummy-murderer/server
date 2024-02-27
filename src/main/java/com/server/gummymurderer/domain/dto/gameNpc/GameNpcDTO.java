package com.server.gummymurderer.domain.dto.gameNpc;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.enum_class.NpcStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameNpcDTO {

    private Long gameNpcNo;
    private String npcName;
    private String npcJob;
    private String npcPersonality;
    private String npcFeature;
    private NpcStatus npcStatus;
    private String deathLocation;
    private long npcDeathNightNumber;


    public GameNpcDTO(GameNpc gameNpc) {
        this.gameNpcNo = gameNpc.getGameNpcNo();
        this.npcName = gameNpc.getNpcName();
        this.npcJob = gameNpc.getNpcJob();
        this.npcPersonality = gameNpc.getNpcPersonality();
        this.npcFeature = gameNpc.getNpcFeature();
        this.npcStatus = gameNpc.getNpcStatus();
        this.deathLocation = gameNpc.getDeathLocation();
        this.npcDeathNightNumber = gameNpc.getNpcDeathNightNumber();
    }

    public String getNpcStatus() {
        return this.npcStatus.name();
    }


}
