package com.server.gummymurderer.domain.dto.question;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AIQuestionCreateRequest {

    private Long gameNo;
    private String npcName;
    private String keyWord = "";
    private String keyWordType = "";

}
