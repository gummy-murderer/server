package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "interrogation_tb")
public class Interrogation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interrogation_no")
    private Long interrogationNo;

    private String npcName;

    private String weapon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

    @OneToMany(mappedBy = "interrogation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterrogationDialogue> dialogues = new ArrayList<>();

    public void addDialogue(InterrogationDialogue dialogue) {
        dialogues.add(dialogue);
    }

}
