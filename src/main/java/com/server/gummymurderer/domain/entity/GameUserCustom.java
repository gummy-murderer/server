package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_user_custom_tb")
public class GameUserCustom extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_custom_no")
    private Long userCustomNo;

    @Column(name = "eyes")
    private int eyes;

    @Column(name = "mouth")
    private int mouth;

    @Column(name = "ears")
    private int ears;

    @Column(name = "body")
    private int body;

    @Column(name = "tail")
    private int tail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

}
