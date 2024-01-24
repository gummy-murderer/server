package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.dto.game.SaveGameRequest;
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
    private Long gameVoteEventNo;

    @Column(name = "vote_npc_name")
    private String voteNpcName;

    @Column(name = "vote_result")
    private String voteResult;

    @Column(name = "vote_night_number")
    private Long voteNightNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

    public GameVoteEvent(SaveGameRequest saveGameRequest, GameSet gameSet) {
        this.voteNpcName = saveGameRequest.getVoteNpcName();
        this.voteResult = saveGameRequest.getVoteResult();
        this.voteNightNumber = saveGameRequest.getVoteNightNumber();
        this.gameSet = gameSet;
    }
}
