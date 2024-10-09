package com.server.gummymurderer.domain.dto.interrogation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterrogationProceedRequest {

    private Long gameSetNo;
    private String npcName;
    private String content;

}
