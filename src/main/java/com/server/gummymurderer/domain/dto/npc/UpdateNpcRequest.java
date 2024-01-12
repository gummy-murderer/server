package com.server.gummymurderer.domain.dto.npc;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateNpcRequest {
    private String npcName;
    private String npcPersonality;
    private String npcPersonalityDescription;
    private String npcFeature;
    private String npcFeatureDescription;
}
