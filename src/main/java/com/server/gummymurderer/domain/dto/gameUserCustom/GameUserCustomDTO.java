package com.server.gummymurderer.domain.dto.gameUserCustom;

import com.server.gummymurderer.domain.entity.GameUserCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameUserCustomDTO {

    private int eyes;
    private int mouth;
    private int ears;
    private int body;
    private int tail;

    public GameUserCustomDTO(GameUserCustom custom) {
        this.eyes = custom.getEyes();
        this.mouth = custom.getMouth();
        this.ears = custom.getEars();
        this.body = custom.getBody();
        this.tail = custom.getTail();
    }

}

