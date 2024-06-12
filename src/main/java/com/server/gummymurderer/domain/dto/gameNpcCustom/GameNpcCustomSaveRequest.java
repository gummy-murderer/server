package com.server.gummymurderer.domain.dto.gameNpcCustom;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameNpcCustom;
import com.server.gummymurderer.domain.entity.GameSet;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameNpcCustomSaveRequest {

    private Long gameSetNo;
    private String npcName;
    private String mouth;
    private String ear;
    private String body;
    private String tail;

    public GameNpcCustom toEntity(GameSet gameSet, GameNpc gameNpc) {

        return GameNpcCustom.builder()
                .gameSet(gameSet)
                .gameNpc(gameNpc)
                .mouth(mouth)
                .ear(ear)
                .body(body)
                .tail(tail)
                .build();

    }
}
