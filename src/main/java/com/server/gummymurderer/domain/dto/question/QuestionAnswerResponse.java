package com.server.gummymurderer.domain.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.patterns.TypePatternQuestions;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerResponse {

    private String response;
    private String language;

    public static QuestionAnswerResponse of(QuestionAnswerResponse ai, String language) {
        return new QuestionAnswerResponse(ai.getResponse(), language);
    }

}
