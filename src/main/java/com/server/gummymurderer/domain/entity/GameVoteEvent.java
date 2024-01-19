package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_vote_evnet_tb")
public class GameVoteEvent extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_vote_event_no")
    private long gameVoteEventNo;

    @Column(name = "vote_npc_name")
    private String voteNpcName;

    @Column(name = "vote_result")
    private String voteResult;

    @Column(name = "vote_night_number")
    private long voteNightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;
}
