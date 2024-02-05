package com.server.gummymurderer.domain.dto.scenario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiMakeScenarioResponse {

    private AnswerDTO answer;
    private TokensDTO tokens;

}
