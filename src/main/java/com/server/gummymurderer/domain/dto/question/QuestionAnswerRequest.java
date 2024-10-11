package com.server.gummymurderer.domain.dto.question;

import com.server.gummymurderer.domain.entity.Question;
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
    private Integer questionIndex;
    private String keyWord = "";
    private String keyWordType = "";

    public QuestionAnswer toEntity(Question question, String answerText) {
        return QuestionAnswer.builder()
                .question(question)
                .answerText(answerText)
                .build();
    }

}
