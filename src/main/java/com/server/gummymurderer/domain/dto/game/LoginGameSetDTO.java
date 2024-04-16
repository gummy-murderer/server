package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomDTO;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class LoginGameSetDTO {

    private Long gameSetNo;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int gameDay;
    private String gameStatus;
    private String gameResult;
    private GameUserCustomDTO custom;

    public LoginGameSetDTO(GameSet gameSet, GameUserCustom custom) {
        this.gameSetNo = gameSet.getGameSetNo();
        this.gameDay = gameSet.getGameDay();
        this.gameStatus = gameSet.getGameStatus().name();
        this.gameResult = gameSet.getGameResult().name();
        this.createdAt = gameSet.getCreatedAt();
        this.modifiedAt = gameSet.getLastModifiedAt();
        this.custom = custom != null ? new GameUserCustomDTO(custom) : null;
    }
}
