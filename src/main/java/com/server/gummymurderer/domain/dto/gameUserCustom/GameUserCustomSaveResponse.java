package com.server.gummymurderer.domain.dto.gameUserCustom;

import com.server.gummymurderer.domain.entity.GameUserCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameUserCustomSaveResponse {

    private boolean isSaved; // 저장 완료 여부
    private LocalDateTime saveTime; // 저장 시간

    public GameUserCustomSaveResponse(GameUserCustom gameUserCustom) {
        this.isSaved = true;
        this.saveTime = gameUserCustom.getCreatedAt();
    }

}
