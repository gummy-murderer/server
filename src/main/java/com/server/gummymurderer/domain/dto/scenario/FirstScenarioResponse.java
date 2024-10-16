package com.server.gummymurderer.domain.dto.scenario;

import com.server.gummymurderer.domain.dto.gameNpc.GameNpcDTO;
import com.server.gummymurderer.domain.entity.GameScenario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FirstScenarioResponse {

    private String crimeScene;
    private String method;
    private String victim;
    private List<GameNpcDTO> gameNpcList;

    public static FirstScenarioResponse of(GameScenario savedGameScenario, List<GameNpcDTO> gameNpcList) {
        FirstScenarioResponse response = new FirstScenarioResponse();
        response.crimeScene = savedGameScenario.getCrimeScene();
        response.method = savedGameScenario.getMethod();
        response.victim = savedGameScenario.getVictim();
        response.gameNpcList = gameNpcList;
        return response;
    }
}
