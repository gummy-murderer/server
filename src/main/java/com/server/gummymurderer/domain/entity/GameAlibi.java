package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_alibi_tb")
public class GameAlibi extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "alibi_no")
    private Long alibiNo;

    private String alibi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_no")
    private GameScenario gameScenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_npc_no")
    private GameNpc gameNpc;
}
