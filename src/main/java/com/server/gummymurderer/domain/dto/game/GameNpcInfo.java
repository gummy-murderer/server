package com.server.gummymurderer.domain.dto.game;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameNpcInfo {

    private String npcName;
    private String npcJob;

}
