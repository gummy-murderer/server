package com.server.gummymurderer.domain.dto.alibi;

import com.server.gummymurderer.domain.entity.GameAlibi;
import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameScenario;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlibiDTO {

    private String name;
    private String alibi;
    private Long gameNpcNo;

    public GameAlibi toEntity(GameScenario gameScenario, GameNpc gameNpc) {
        return GameAlibi.builder()
                .alibi(alibi)
                .gameScenario(gameScenario)
                .gameNpc(gameNpc)
                .build();
    }

    public static AlibiDTO of(GameAlibi gameAlibi) {
        AlibiDTO alibiDTO = new AlibiDTO();
        alibiDTO.name = gameAlibi.getGameNpc().getNpcName();
        alibiDTO.alibi = gameAlibi.getAlibi();
        alibiDTO.gameNpcNo = gameAlibi.getGameNpc().getGameNpcNo();
        return alibiDTO;
    }
}
