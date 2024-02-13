package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.NpcStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_npc_tb")
public class GameNpc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_npc_no")
    private long gameNpcNo;

    @Column(name = "npc_name")
    private String npcName;

    @Column(name = "npc_job")
    private String npcJob;

    @Column(name = "npc_personality")
    private String npcPersonality;

    @Column(name = "npc_feature")
    private String npcFeature;

    @Enumerated(EnumType.STRING)
    @Column(name = "npc_status")
    private NpcStatus npcStatus;

    @Column(name = "npc_death_location_x")
    private float npcDeathLocationX;

    @Column(name = "npc_death_location_y")
    private float npcDeathLocationY;

    @Column(name = "npc_death_location_z")
    private float npcDeathLocationZ;

    @Column(name = "npc_death_night_number")
    private long npcDeathNightNumber;

    @Column(name = "prompt_tokens")
    private long gameNpcPromptTokens;

    @Column(name = "completion_tokens")
    private long gameNpcCompletionTokens;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;


    public GameNpc(Npc npc, String npcJob, GameSet gameSet) {
        this.npcName = npc.getNpcName();
        this.npcJob = npcJob;
        this.npcPersonality = npc.getNpcPersonality();
        this.npcFeature = npc.getNpcFeature();
        this.npcStatus = NpcStatus.ALIVE;
        this.npcDeathLocationX = 0;
        this.npcDeathLocationY = 0;
        this.npcDeathLocationZ = 0;
        this.npcDeathNightNumber = 0;
        this.gameSet = gameSet;
    }

    public void voteEvent() {
        this.npcStatus = NpcStatus.DEAD;
    }

    public void dead() {
        this.npcStatus = NpcStatus.DEAD;
    }

    public void updateTokens(long promptTokens, long completionTokens) {
        this.gameNpcPromptTokens += promptTokens;
        this.gameNpcCompletionTokens += completionTokens;
    }

}
