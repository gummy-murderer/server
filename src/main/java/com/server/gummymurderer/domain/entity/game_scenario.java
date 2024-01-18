package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_scenario_tb")
public class game_scenario extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "senario_no")
    private long scenarioNo;

    @Column(name = "victim")
    private String victim;

    @Column(name = "crime_scene")
    private String crimeScene;

    @Column(name = "method")
    private String method;

    @Column(name = "witness")
    private String witness;

    @Column(name = "testimony")
    private String testimony;

    @Column(name = "body_condition")
    private String bodyCondition;

    @Column(name = "daily_summary")
    private String dailySummary;

    @Column(name = "scenario_token")
    private long scenarioToken;

}
