package com.server.gummymurderer.domain.dto.gameNpc;

import com.server.gummymurderer.domain.entity.Npc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameNpcInfoResponse {

    private Long gameNpcNo;
    private String npcName;
    private int age;
    private String gender;
    private String npcFeatures;
    private String npcFeaturesDescription;
    private String npcPersonality;
    private String npcPersonalityDescription;


    public static GameNpcInfoResponse of(Long gameNpcNo, Npc npc) {

        return new GameNpcInfoResponse(
                gameNpcNo,
                npc.getNpcName(),
                npc.getAge(),
                npc.getGender(),
                npc.getNpcFeature(),
                npc.getNpcFeatureDescription(),
                npc.getNpcPersonality(),
                npc.getNpcPersonalityDescription()
        );
    }
}
