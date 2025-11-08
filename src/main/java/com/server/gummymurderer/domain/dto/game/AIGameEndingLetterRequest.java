package com.server.gummymurderer.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIGameEndingLetterRequest {

    private Long gameNo;
    private String language;
    private String gameResult;

    public static AIGameEndingLetterRequest create(Long gameNo, String gameResult, String language) {
        return AIGameEndingLetterRequest.builder()
                .gameNo(gameNo)
                .language(language)
                .gameResult(gameResult)
                .build();
    }

}
