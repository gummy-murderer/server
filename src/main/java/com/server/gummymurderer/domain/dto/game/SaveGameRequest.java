package com.server.gummymurderer.domain.dto.game;

import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListRequest;
import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveGameRequest {

    private Long gameSetNo;
    private int gameDay;
    private String voteNpcName;
    private String voteResult;
    private Long voteNightNumber;
    private List<CheckListRequest> checkList;
    private GameUserCustomSaveRequest custom;

}
