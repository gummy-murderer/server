package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.dto.alibi.AlibiDTO;
import com.server.gummymurderer.domain.dto.gameNpcCustom.NpcCustomInfo;
import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookSaveResponse;
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
    private List<DetectiveNotebookSaveResponse> notebookRequestList;
    private List<AlibiDTO> alibi;
    private MakeScenarioResponse scenario; // scenario, npcList
    private List<NpcCustomInfo> npcCustomInfos;

    public static LoadGameResponse of(LoginGameSetDTO gameSet, String deadNpc, String deadPlace,
                                      List<DetectiveNotebookSaveResponse> notebookRequestList, List<AlibiDTO> alibi,
                                      MakeScenarioResponse scenario, List<NpcCustomInfo> npcCustomInfos) {
        return new LoadGameResponse(gameSet, deadNpc, deadPlace, notebookRequestList, alibi, scenario, npcCustomInfos);
    }

}
