package com.server.gummymurderer.domain.dto.scenario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinalWordAIRequest {

    private Long gameNo;
    private String language;
    private String gameResult;

    public static FinalWordAIRequest of(Long gameNo, String language, String gameResult) {
        return new FinalWordAIRequest(gameNo, language, gameResult);
    }

}
