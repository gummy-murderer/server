package com.server.gummymurderer.domain.entity;


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
    @Column(name = "npc_features")
    private String npcFeature;
}
