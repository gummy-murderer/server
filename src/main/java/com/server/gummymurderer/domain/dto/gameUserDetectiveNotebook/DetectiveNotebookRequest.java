package com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserDetectiveNotebook;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DetectiveNotebookRequest {

    private String npcName;
    private List<Integer> time;
    private List<Integer> place;
    private List<Integer> weapon;
    private String contents;

    public GameUserDetectiveNotebook toEntity(GameNpc gameNpc, GameSet gameSet) {
        return GameUserDetectiveNotebook.builder()
                .time(GameUserDetectiveNotebook.listToString(time)) // List → String
                .place(GameUserDetectiveNotebook.listToString(place))
                .weapon(GameUserDetectiveNotebook.listToString(weapon))
                .contents(contents)
                .gameNpc(gameNpc)
                .gameSet(gameSet)
                .build();
    }

}