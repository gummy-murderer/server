package com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectiveNotebookSaveRequest {

    private Long gameSetNo;
    private List<DetectiveNotebookRequest> notebookRequestList;

}