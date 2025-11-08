package com.server.gummymurderer.domain.dto.scenario;

import com.server.gummymurderer.domain.dto.game.GameNpcInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIMakeScenarioRequest {

    private Long gameSetNo;
    private String language;
    private List<GameNpcInfo> livingCharacters;

}
