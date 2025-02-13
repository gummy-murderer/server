package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.dto.gameNpcCustom.NpcCustomInfo;
import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookRequest;
import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveRequest;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaveGameRequest {

    private Long gameSetNo;
    private int gameDay;
    private String voteNpcName;
    private boolean voteResult;
    private Long voteNightNumber;
    private List<DetectiveNotebookRequest> notebookRequestList;
    private GameUserCustomSaveRequest userCustom;
    private List<NpcCustomInfo> npcCustomInfos;

}