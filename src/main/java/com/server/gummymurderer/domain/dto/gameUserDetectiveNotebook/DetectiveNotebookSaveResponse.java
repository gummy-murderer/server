package com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook;

import com.server.gummymurderer.domain.entity.GameUserDetectiveNotebook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetectiveNotebookSaveResponse {
    private Long userDetectiveNotebookNo;
    private List<Integer> time;
    private List<Integer> place;
    private List<Integer> weapon;
    private String contents;
    private String npcName;

    public DetectiveNotebookSaveResponse(GameUserDetectiveNotebook gameUserDetectiveNotebook) {
        this.userDetectiveNotebookNo = gameUserDetectiveNotebook.getUserDetectiveNotebookNo();
        this.time = GameUserDetectiveNotebook.stringToList(gameUserDetectiveNotebook.getTime());
        this.place = GameUserDetectiveNotebook.stringToList(gameUserDetectiveNotebook.getPlace());
        this.weapon = GameUserDetectiveNotebook.stringToList(gameUserDetectiveNotebook.getWeapon());
        this.contents = gameUserDetectiveNotebook.getContents();
        this.npcName = gameUserDetectiveNotebook.getGameNpc().getNpcName();
    }

    public static DetectiveNotebookSaveResponse of(GameUserDetectiveNotebook gameUserDetectiveNotebook) {
        return new DetectiveNotebookSaveResponse(gameUserDetectiveNotebook);
    }
}