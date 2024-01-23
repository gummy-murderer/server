package com.server.gummymurderer.domain.dto.gameUserCheckList;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameUserCheckList;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckListSaveRequest {

    private String mark;
    private String checkJob;
    private Long gameNpcNo;

    public static GameUserCheckList toEntity(CheckListSaveRequest request, GameNpc gameNpc) {
        return GameUserCheckList.builder()
                .mark(request.mark)
                .checkJob(request.checkJob)
                .gameNpc(gameNpc)
                .build();
    }

}
