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
    private String gameResult;

    public static AIGameEndingLetterRequest create(Long gameNo, String gameResult) {
        return AIGameEndingLetterRequest.builder()
                .gameNo(gameNo)
                .gameResult(gameResult)
                .build();
    }

}
