package com.server.gummymurderer.domain.dto.gameUserCheckList;

import com.server.gummymurderer.domain.entity.GameUserCheckList;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckListSaveResponse {
    private Long userChecklistNo;
    private String mark;
    private String checkJob;
    private String npcName;

    public CheckListSaveResponse(GameUserCheckList gameUserCheckList) {
        this.userChecklistNo = gameUserCheckList.getUserChecklistNo();
        this.mark = gameUserCheckList.getMark();
        this.checkJob = gameUserCheckList.getCheckJob();
        this.npcName = gameUserCheckList.getGameNpc().getNpcName();
    }

    public static CheckListSaveResponse of(GameUserCheckList gameUserCheckList) {
        return new CheckListSaveResponse(gameUserCheckList);
    }
}