package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.entity.GameSet;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveGameResponse {

    private boolean isSaved; // 저장 완료 여부
    private LocalDateTime saveTime; // 저장 시간
    private int saveDay; // 저장 날짜

    public SaveGameResponse(GameSet gameSet) {
        this.isSaved = true;
        this.saveTime = gameSet.getLastModifiedAt();
        this.saveDay = gameSet.getGameDay();
    }
}
