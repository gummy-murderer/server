package com.server.gummymurderer.domain.dto.gameNpcCustom;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameNpcCustomSaveRequest {

    private Long gameSetNo;
    private List<NpcCustomInfo> npcCustomInfos;
}
