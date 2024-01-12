package com.server.gummymurderer.domain.entity;


import com.server.gummymurderer.domain.dto.npc.UpdateNpcRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "npc_tb")
public class Npc extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npc_no")
    private long npcNo;

    @NotBlank
    @Column(name = "npc_name")
    private String npcName;

    @NotBlank
    @Column(name = "npc_personality")
    private String npcPersonality;

    @NotBlank
    @Column(name = "npc_personality_description")
    private String npcPersonalityDescription;
    @NotBlank
    @Column(name = "npc_features")
    private String npcFeature;

    @NotBlank
    @Column(name = "npc_features_description")
    private String npcFeatureDescription;

    public void updateNpc(UpdateNpcRequest request) {
        this.npcName = request.getNpcName();
        this.npcPersonality = request.getNpcPersonality();
        this.npcPersonalityDescription = request.getNpcPersonalityDescription();
        this.npcFeature = request.getNpcFeature();
    }
}
