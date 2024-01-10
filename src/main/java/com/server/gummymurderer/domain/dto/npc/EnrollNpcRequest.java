package com.server.gummymurderer.domain.dto.npc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class EnrollNpcRequest {

    private String npcName;
    private String npcPersonality;
    private String npcFeature;

}
