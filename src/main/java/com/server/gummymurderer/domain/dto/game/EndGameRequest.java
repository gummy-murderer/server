package com.server.gummymurderer.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndGameRequest {

    private Long gameSetNo;
    private String resultMessage;
}
