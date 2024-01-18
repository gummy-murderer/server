package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_set_tb")
public class GameSet extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gmae_set_no")
    private long gameSetNo;

    @Column(name = "game_status")
    private String gameStatus;

    @Column(name = "game_token")
    private long gameToken;

    @Column(name = "game_chat_summary")
    private long gameSummary;
}
