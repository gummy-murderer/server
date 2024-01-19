package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_user_checklist_tb")
public class GameUserChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_checklist_no")
    private long userChecklistNo;

    @Column(name = "check_job")
    private String checkJob;

    @Column(name = "confirmed_job")
    private String confirmedJob;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_npc_no")
    private GameNpc gameNpc;
}
