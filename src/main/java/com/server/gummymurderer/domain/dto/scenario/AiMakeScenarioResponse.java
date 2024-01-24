package com.server.gummymurderer.domain.dto;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerDTO {
        private String victim;
        private String crimeScene;
        private String method;
        private String witness;
        private String eyewitnessInformation;
        private String dailySummary;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokensDTO {
        private int totalTokens;
        private int promptTokens;
        private int completionTokens;
        private String totalCostUSD;
    }
}
