package com.server.gummymurderer.domain.dto.question;

import com.server.gummymurderer.domain.entity.QuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerRequest {

    private Long gameSetNo;
    private String npcName;
    private String keyWord = "";
    private String keyWordType = "";

    public QuestionAnswer toEntity(String answerText) {
        return QuestionAnswer.builder()
                .answerText(answerText)
                .build();
    }

}
