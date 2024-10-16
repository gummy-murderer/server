package com.server.gummymurderer.domain.dto.game;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartGameAIRequest {

    private Long gameNo;
    private String language;
    private List<GameNpcInfo> characters;

    public static StartGameAIRequest create(Long gameNo, String language,  List<GameNpcInfo> npcList) {
        return StartGameAIRequest.builder()
                .gameNo(gameNo)
                .language(language)
                .characters(npcList)
                .build();
    }
}
