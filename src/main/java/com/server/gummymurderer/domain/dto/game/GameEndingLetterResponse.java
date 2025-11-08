package com.server.gummymurderer.domain.dto.game;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameEndingLetterResponse {

    private String result;
    private String language;
    private Letter chiefLetter;
    private Letter murdererLetter;
    private List<SurvivorLetter> survivorsLetters;

    public static GameEndingLetterResponse of(GameEndingLetterResponse aiResponse, String language) {
        return new GameEndingLetterResponse(
                aiResponse.getResult(),
                language,
                aiResponse.getChiefLetter(),
                aiResponse.getMurdererLetter(),
                aiResponse.getSurvivorsLetters()
        );
    }

}