package com.server.gummymurderer.domain.dto.scenario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntroAndScenarioResponse {

    private IntroAnswerDTO introAnswer;
    private MakeScenarioResponse scenarioResponse;

}
