package com.server.gummymurderer.domain.dto.chat;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcChatRequest {

    private String npcName1;
    private String npcPersonality1;
    private String npcPersonalityDescription1;
    private String npcFeature1;
    private String npcFeatureDescription1;

    private String npcName2;
    private String npcPersonality2;
    private String npcPersonalityDescription2;
    private String npcFeature2;
    private String npcFeatureDescription2;;

}
