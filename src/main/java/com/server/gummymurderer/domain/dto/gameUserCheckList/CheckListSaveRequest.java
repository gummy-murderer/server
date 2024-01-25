package com.server.gummymurderer.domain.dto.gameUserCheckList;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameUserCheckList;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckListSaveRequest {

    private Long gameSetNo;
    private List<CheckListRequest> checkList;

}