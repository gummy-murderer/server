package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_user_checklist_tb")
public class GameUserCheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_checklist_no")
    private Long userChecklistNo;

    @Column(name = "mark")
    private String mark;  // 수정(○, △, X) ->

    @Column(name = "check_job")
    private String checkJob; // 수정(사용자가 끌어다 놓은 직업)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_npc_no")
    private GameNpc gameNpc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;
}
