package com.server.gummymurderer.domain.dto.scenario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinalWordRequest {

    private Long gameSetNo;
    private String secretKey;
    private String gameResult;
    private String murderer;
    private List<String> livingCharacters = new ArrayList<>();
    private String previousStory;

}
