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
    private Long gameStatus;

    public LoginGameSetDTO(GameSet gameSet) {
        this.gameSetNo = gameSet.getGameSetNo();
        this.gameStatus = gameSet.getGameStatus();
        this.createdAt = gameSet.getCreatedAt();
        this.modifiedAt = gameSet.getLastModifiedAt();
    }
}
