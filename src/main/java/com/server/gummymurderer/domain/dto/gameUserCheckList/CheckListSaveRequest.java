package com.server.gummymurderer.domain.dto.gameUserCheckList;

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