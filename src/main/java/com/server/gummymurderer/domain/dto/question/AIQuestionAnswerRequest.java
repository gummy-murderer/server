package com.server.gummymurderer.domain.dto.question;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AIQuestionAnswerRequest {

    private Long gameNo;
    private String npcName;
    private Integer questionIndex;
    private String keyWord;
    private String keyWordType;

}
