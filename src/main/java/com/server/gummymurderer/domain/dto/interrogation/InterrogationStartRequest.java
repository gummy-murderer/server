package com.server.gummymurderer.domain.dto.interrogation;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Interrogation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterrogationStartRequest {

    private Long gameSetNo;
    private String npcName;
    private String murderWeapon;
    private String murderLocation;
    private String murderTime;

    public Interrogation toEntity(GameSet gameSet) {
        return Interrogation.builder()
                .npcName(this.npcName)
                .murdererWeapon(this.murderWeapon)
                .murdererLocation(this.murderLocation)
                .murderTime(this.murderTime)
                .gameSet(gameSet)
                .build();
    }

}

