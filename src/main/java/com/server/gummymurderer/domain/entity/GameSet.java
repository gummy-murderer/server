package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.GameStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_set_tb")
public class GameSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_set_no")
    private long gameSetNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_status")
    private GameStatus gameStatus;

    @Column(name = "game_day")
    private int gameDay;

    @Column(name = "game_token")
    private long gameToken;

    @Column(name = "game_chat_summary")
    private String gameSummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    public void updateGameToken(long gameToken) {
        this.gameToken = gameToken;
    }

    public void endGameStatus() {
        this.gameStatus = GameStatus.GAME_END;
    }
}
