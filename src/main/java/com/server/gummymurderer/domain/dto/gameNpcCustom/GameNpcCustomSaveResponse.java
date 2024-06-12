package com.server.gummymurderer.domain.dto.gameNpcCustom;

import com.server.gummymurderer.domain.entity.GameNpcCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameNpcCustomSaveResponse {

    private boolean isSaved;
    private LocalDateTime saveTime;

    public GameNpcCustomSaveResponse(GameNpcCustom custom) {
        this.isSaved = true;
        this.saveTime = custom.getCreatedAt();
    }

}
