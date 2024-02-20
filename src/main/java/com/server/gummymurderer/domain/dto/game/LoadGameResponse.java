package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.dto.alibi.AlibiDTO;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveResponse;
import com.server.gummymurderer.domain.dto.scenario.MakeScenarioResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoadGameResponse {

    private LoginGameSetDTO gameSet;
    private String deadNpc;
    private String deadPlace;
    private List<CheckListSaveResponse> checkList;
    private List<AlibiDTO> alibi;
    private MakeScenarioResponse scenario; // scenario, npcList

    public static LoadGameResponse of(LoginGameSetDTO gameSet, String deadNpc, String deadPlace, List<CheckListSaveResponse> checkList, List<AlibiDTO> alibi, MakeScenarioResponse scenario) {
        return new LoadGameResponse(gameSet, deadNpc, deadPlace, checkList, alibi, scenario);
    }

}
