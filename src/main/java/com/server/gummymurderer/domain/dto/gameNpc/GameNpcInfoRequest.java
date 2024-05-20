package com.server.gummymurderer.domain.dto.gameNpc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameNpcInfoRequest {

    private Long gameSetNo;
    private Long gameNpcNo;

}
