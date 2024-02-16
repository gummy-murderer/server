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
public class MakeScenarioResponse {

    private String crimeScene;
    private String dailySummary;
    private String victim;
    private List<GameNpcDTO> gameNpcList;

   public static MakeScenarioResponse of(GameScenario savedGameScenario, List<GameNpcDTO> gameNpcList) {
       MakeScenarioResponse response = new MakeScenarioResponse();
       response.crimeScene = savedGameScenario.getCrimeScene();
       response.dailySummary = savedGameScenario.getDailySummary();
       response.victim = savedGameScenario.getVictim();
       response.gameNpcList = gameNpcList;
       return response;
   }
}
