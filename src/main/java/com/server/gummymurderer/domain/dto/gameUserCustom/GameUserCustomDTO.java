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

    private String head;
    private String eye;
    private String mouth;
    private String ear;
    private String body;
    private String tail;

    public GameUserCustomDTO(GameUserCustom custom) {
        this.head = custom.getHead();
        this.eye = custom.getEye();
        this.mouth = custom.getMouth();
        this.ear = custom.getEar();
        this.body = custom.getBody();
        this.tail = custom.getTail();
    }

}

