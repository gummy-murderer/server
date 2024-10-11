package com.server.gummymurderer.domain.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateResponse {

    private List<QuestionCreateDTO> questions;

    public static QuestionCreateResponse from(QuestionCreateResponse response) {
        return new QuestionCreateResponse(response.getQuestions());
    }

}
