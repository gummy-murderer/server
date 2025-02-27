package com.server.gummymurderer.domain.dto.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameAutopsyResponse {

    @JsonProperty("answer")
    private Answer answer; // 그대로 매핑

    @Data
    public static class Answer {
        private String victim;
        private String crimeTime;
        private String crimeScene;
        private String method;
        private InvestigationReport investigationReport;
    }

    @Data
    public static class InvestigationReport {
        private String timeAnalysis;
        private String methodAnalysis;
        private String sceneAnalysis;
    }

}