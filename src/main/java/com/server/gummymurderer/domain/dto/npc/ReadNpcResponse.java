package com.server.gummymurderer.domain.dto.npc;

import com.server.gummymurderer.domain.entity.Npc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadNpcResponse {
    private Long npcNo;
    private String npcName;
    private String npcPersonality;
    private String npcFeature;
    private LocalDateTime npcCreatedAt;
    private LocalDateTime npcLastModifiedAt;
    private LocalDateTime npcDeletedAt;

    public ReadNpcResponse(Npc npc) {
        this.npcNo = npc.getNpcNo();
        this.npcName = npc.getNpcName();
        this.npcPersonality = npc.getNpcPersonality();
        this.npcFeature = npc.getNpcFeature();
        this.npcCreatedAt = npc.getCreatedAt();
        this.npcLastModifiedAt = npc.getLastModifiedAt();
        this.npcDeletedAt = npc.getDeletedAt();
    }
}
