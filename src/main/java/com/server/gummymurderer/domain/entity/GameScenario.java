package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.dto.scenario.AiMakeScenarioResponse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_scenario_tb")
public class GameScenario extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_no")
    private Long scenarioNo;

    @Column(name = "victim")
    private String victim;

    @Column(name = "crime_scene")
    private String crimeScene;

    @Column(name = "method")
    private String method;

    @Column(name = "witness")
    private String witness;

    @Column(name = "eyewitness_information")
    private String eyewitnessInformation;
    @Column(name = "daily_summary")
    private String dailySummary;

    @Column(name = "scenario_prompt_token")
    private long scenarioPromptToken;

    @Column(name = "scenario_completion_token")
    private long scenarioCompletionToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

    public GameScenario(AiMakeScenarioResponse result, GameSet gameSet) {
        this.victim = result.getAnswer().getVictim();
        this.crimeScene = result.getAnswer().getCrimeScene();
        this.method = result.getAnswer().getMethod();
        this.witness = result.getAnswer().getWitness();
        this.eyewitnessInformation = result.getAnswer().getEyewitnessInformation();
        this.dailySummary = result.getAnswer().getDailySummary();
        this.gameSet = gameSet;
    }
}
