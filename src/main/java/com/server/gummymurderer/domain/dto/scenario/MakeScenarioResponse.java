package com.server.gummymurderer.domain.dto.scenario;

import com.server.gummymurderer.domain.entity.GameScenario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MakeScenarioResponse {


    private String crimeScene;
    private String dailySummary;
    private String victim;

    public MakeScenarioResponse(GameScenario savedGameScenario) {
        this.crimeScene = savedGameScenario.getCrimeScene();
        this.dailySummary = savedGameScenario.getDailySummary();
        this.victim = savedGameScenario.getVictim();
    }

}
