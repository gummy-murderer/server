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
    private Letter chiefLetter;
    private Letter murdererLetter;
    private List<SurvivorLetter> survivorsLetters;


}