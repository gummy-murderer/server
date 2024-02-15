package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.entity.GameSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LoginGameSetDTO {

    private Long gameSetNo;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int gameDay;
    private String gameStatus;
    private String gameResult;

    public LoginGameSetDTO(GameSet gameSet) {
        this.gameSetNo = gameSet.getGameSetNo();
        this.gameDay = gameSet.getGameDay();
        this.gameStatus = gameSet.getGameStatus().name();
        this.gameResult = gameSet.getGameResult().name();
        this.createdAt = gameSet.getCreatedAt();
        this.modifiedAt = gameSet.getLastModifiedAt();
    }
}
