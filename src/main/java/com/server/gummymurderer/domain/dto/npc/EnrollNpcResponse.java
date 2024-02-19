package com.server.gummymurderer.domain.dto.npc;

import com.server.gummymurderer.domain.entity.Npc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollNpcResponse {

    private Long npcNo;
    private String npcName;
    private String npcPersonality;
    private String npcPersonalityDescription;
    private String npcFeature;
    private String npcFeatureDescription;

    public EnrollNpcResponse(Npc npc) {
        this.npcNo = npc.getNpcNo();
        this.npcName = npc.getNpcName();
        this.npcPersonality = npc.getNpcPersonality();
        this.npcPersonalityDescription = npc.getNpcPersonalityDescription();
        this.npcFeature = npc.getNpcFeature();
        this.npcFeatureDescription = npc.getNpcFeatureDescription();
    }
}
