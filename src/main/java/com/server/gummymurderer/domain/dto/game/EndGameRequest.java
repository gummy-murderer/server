package com.server.gummymurderer.domain.dto.game;

import lombok.Getter;

@Getter
public class EndGameRequest {

    private Long gameSetNo;
    private String resultMessage;
}
